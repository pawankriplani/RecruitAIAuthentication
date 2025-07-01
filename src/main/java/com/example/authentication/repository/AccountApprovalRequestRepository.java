package com.example.authentication.repository;

import com.example.authentication.model.AccountApprovalRequest;
import com.example.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountApprovalRequestRepository extends JpaRepository<AccountApprovalRequest, Integer> {
    List<AccountApprovalRequest> findByUser(User user);
    List<AccountApprovalRequest> findByStatus(AccountApprovalRequest.Status status);
    boolean existsByUserUserIdAndStatus(Integer userId, AccountApprovalRequest.Status status);
}
