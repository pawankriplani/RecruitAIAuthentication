package com.example.authentication.repository;

import com.example.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    @Query("SELECT u.email FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE r.roleName = 'RMG' AND u.isActive = true AND u.accountStatus = 'ACTIVE'")
    Optional<String> findRmgEmail();

    @Query("SELECT u FROM User u WHERE u.accountStatus = 'PENDING'")
    List<User> findPendingUsers();
}
