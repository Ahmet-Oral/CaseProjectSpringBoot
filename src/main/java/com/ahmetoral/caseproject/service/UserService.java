package com.ahmetoral.caseproject.service;

import com.ahmetoral.caseproject.model.Role;
import com.ahmetoral.caseproject.model.User;
import com.ahmetoral.caseproject.requests.UserRequest;

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
