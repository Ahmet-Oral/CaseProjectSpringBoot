package com.ahmetoral.inventorymanagement.repo;

import com.ahmetoral.inventorymanagement.model.FailedLoginAttempt;
import com.ahmetoral.inventorymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FailedLoginAttemptRepo extends JpaRepository<FailedLoginAttempt, Long> {
    Optional<FailedLoginAttempt> findByUser(User user);
}
