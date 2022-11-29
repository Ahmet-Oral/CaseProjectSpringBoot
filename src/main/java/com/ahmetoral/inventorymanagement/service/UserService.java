package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    // todo remove role from user ?
    User getUserByUsername(String username);
    List<User> getUsers(); // todo pagination maybe?
}
