package com.example.authentication.repository;

import com.example.authentication.model.UserRole;
import com.example.authentication.model.UserRole.UserRoleId;
import com.example.authentication.model.User;
import com.example.authentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUser(User user);
    List<UserRole> findByRole(Role role);
    boolean existsByUserAndRole(User user, Role role);
}
