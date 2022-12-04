package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getUsers(); // todo pagination maybe?
    User getUserByUsername(String username);
    void updateUser(User user);
    User saveUser(User user);
    void deleteUserByUsername(String username);
    Boolean checkUsernameExists(String username);
    Role saveRole(Role role);
    Boolean checkRoleExists(String role);

    void addRoleToUser(String username, String roleName);
    User getUserById(UUID id);
}
