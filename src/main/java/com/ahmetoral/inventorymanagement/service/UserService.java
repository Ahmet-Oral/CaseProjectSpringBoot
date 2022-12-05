package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;
import com.ahmetoral.inventorymanagement.requests.UserRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getUsers(); // todo pagination maybe?
    User getUserByUsername(String username);
    void updateUser(UserRequest user);
    void saveUser(UserRequest userRequest);
    void deleteUserById(UUID id);
    Boolean checkUsernameExists(String username);
    Role saveRole(Role role);
    Boolean checkRoleExists(String role);
    void setUserRole(String username, String roleName);
    User getUserById(UUID id);
}
