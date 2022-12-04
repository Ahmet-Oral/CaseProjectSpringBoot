package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.model.FailedLoginAttempt;
import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers(); // todo pagination maybe?
    User getUserByUsername(String username);
    User saveUser(User user);
    Boolean checkUsernameExists(String username);
    Role saveRole(Role role);
    Boolean checkRoleExists(String role);

    void newFailedLoginAttempt(String username);
    FailedLoginAttempt getNumberOfFailedAttemptByUser(User user);
    void addRoleToUser(String username, String roleName);

}
