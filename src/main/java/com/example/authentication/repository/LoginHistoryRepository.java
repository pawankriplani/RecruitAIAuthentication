package com.example.authentication.repository;

import com.example.authentication.model.LoginHistory;
import com.example.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
    List<LoginHistory> findByUser(User user);
    List<LoginHistory> findByUserAndLoginTimeBetween(User user, LocalDateTime start, LocalDateTime end);
    List<LoginHistory> findBySuccessAndLoginTimeBetween(boolean success, LocalDateTime start, LocalDateTime end);
}
