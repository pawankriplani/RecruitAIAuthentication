package com.example.authentication.service.impl;

import java.util.List;
import com.example.authentication.event.UserRegistrationData;
import com.example.authentication.exception.UserAlreadyApprovedException;
import com.example.authentication.model.AccountApprovalRequest;
import com.example.authentication.model.Role;
import com.example.authentication.model.User;
import com.example.authentication.repository.AccountApprovalRequestRepository;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.service.ApprovalRequestService;
import com.example.authentication.service.PubSubService;
import com.example.authentication.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApprovalRequestServiceImpl implements ApprovalRequestService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApprovalRequestServiceImpl.class);

    private final AccountApprovalRequestRepository accountApprovalRequestRepository;
    private final PubSubService pubSubService;
    private final UserRepository userRepository;

    @Autowired
    public ApprovalRequestServiceImpl(AccountApprovalRequestRepository accountApprovalRequestRepository,
                                    PubSubService pubSubService,
                                    UserRepository userRepository) {
        this.accountApprovalRequestRepository = accountApprovalRequestRepository;
        this.pubSubService = pubSubService;
        this.userRepository = userRepository;
    }

    @Override
    public AccountApprovalRequest createApprovalRequest(User user) {
        AccountApprovalRequest approvalRequest = new AccountApprovalRequest();
        approvalRequest.setUser(user);
        approvalRequest.setStatus(AccountApprovalRequest.Status.PENDING);
        approvalRequest.setCreatedBy(user);
        return accountApprovalRequestRepository.save(approvalRequest);
    }

    @Override
    public void notifyApprovalRequest(User user, Role role) {
        String rmgEmail = userRepository.findRmgEmail()
            .orElse(""); // If no RMG user found, use empty string
        UserRegistrationData registrationData = new UserRegistrationData(
            user.getUserId().toString(),
            user.getUsername(),
            user.getEmail(),
            role.getRoleName(),
            Constants.STATUS_PENDING,
            rmgEmail
        );
        pubSubService.publishUserRegistrationEvent(registrationData)
            .exceptionally(throwable -> {
                logger.warn("Failed to publish registration event, but user registration completed successfully", throwable);
                return null;
            });
    }

    @Override
    @Transactional
    public void approveManagerAccount(Integer userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAccountStatus() == User.AccountStatus.ACTIVE) {
            throw new UserAlreadyApprovedException("User is already approved");
        }

        user.setAccountStatus(User.AccountStatus.ACTIVE);
        userRepository.save(user);

        List<AccountApprovalRequest> approvalRequests = accountApprovalRequestRepository.findByUser(user);
        if (approvalRequests.isEmpty()) {
            throw new RuntimeException("Approval request not found");
        }
        AccountApprovalRequest approvalRequest = approvalRequests.get(0);

        approvalRequest.setStatus(AccountApprovalRequest.Status.APPROVED);
        accountApprovalRequestRepository.save(approvalRequest);

        logger.info("Manager account approved for user: {}", user.getUsername());
    }
}
