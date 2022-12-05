package com.ahmetoral.caseproject.repo;

import com.ahmetoral.caseproject.model.FailedLoginAttempt;
import com.ahmetoral.caseproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FailedLoginAttemptRepo extends JpaRepository<FailedLoginAttempt, Long> {
    Optional<FailedLoginAttempt> findByUser(User user);
}
