package com.ahmetoral.caseproject.repo;

import com.ahmetoral.caseproject.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
