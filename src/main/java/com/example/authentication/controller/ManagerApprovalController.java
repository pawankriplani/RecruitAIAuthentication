package com.example.authentication.controller;

import com.example.authentication.dto.UserDto;
import com.example.authentication.exception.UserAlreadyApprovedException;
import com.example.authentication.response.ApiResponse;
import com.example.authentication.service.UserService;
import com.example.authentication.model.User;
import com.example.authentication.service.ApprovalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ManagerApprovalController {
    private static final Logger logger = LoggerFactory.getLogger(ManagerApprovalController.class);

    private final UserService userService;
    private final ApprovalRequestService approvalRequestService;

    @Autowired
    public ManagerApprovalController(UserService userService, ApprovalRequestService approvalRequestService) {
        this.userService = userService;
        this.approvalRequestService = approvalRequestService;
    }

    private static class ApprovalData {
        private String message;
        private String status;

        public ApprovalData(String message, String status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public String getStatus() {
            return status;
        }
    }

    @GetMapping("/pending-approvals")
    public ResponseEntity<ApiResponse<List<UserDto>>> getPendingApprovals() {
        logger.info("Received request for pending approvals");
        List<UserDto> pendingUsers = userService.getPendingUsers();
        logger.info("Retrieved {} pending users", pendingUsers.size());
        return ResponseEntity.ok(ApiResponse.success(pendingUsers, "Pending approvals retrieved successfully"));
    }

   
    @PutMapping("/{userId}/approve")
    public ResponseEntity<ApiResponse<ApprovalData>> approveManagerAccount(@PathVariable Integer userId) {
        logger.info("Received request to approve manager account for user ID: {}", userId);
        try {
            User user = userService.getUserById(userId);
            ApprovalData data;
            
            if (user.getAccountStatus() == User.AccountStatus.PENDING) {
                approvalRequestService.approveManagerAccount(userId);
                data = new ApprovalData("Manager account approved successfully", "ACTIVE");
            } else if (user.getAccountStatus() == User.AccountStatus.ACTIVE) {
                data = new ApprovalData("Manager account is already active", "ACTIVE");
            } else {
                data = new ApprovalData("Unable to approve manager account", user.getAccountStatus().toString());
            }
            
            logger.info("Approval process completed for user ID: {}", userId);
            return ResponseEntity.ok(ApiResponse.success(data, data.getMessage()));
        } catch (RuntimeException e) {
            logger.error("Failed to approve manager account for user ID: {}", userId, e);
            ApprovalData data = new ApprovalData("Failed to approve manager account: " + e.getMessage(), "ERROR");
            return ResponseEntity.badRequest().body(ApiResponse.error(data.getMessage(), 400));
        }
    }
}
