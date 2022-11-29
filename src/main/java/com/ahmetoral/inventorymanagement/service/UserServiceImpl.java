package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.exception.ApiRequestException;
import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;
import com.ahmetoral.inventorymanagement.repo.RoleRepo;
import com.ahmetoral.inventorymanagement.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // leaving dependency injection to Lombok,
@Transactional
@Slf4j // logging
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users from database");
        return userRepo.findAll();
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user with username: {} to the database", user.getUsername());
        // todo user validation
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role: {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role: {} to user with username: {} the database", roleName, username);
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException("User with username:" + username + " does not exist"));
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new ApiRequestException("Role:" + roleName + " does not exist"));

//        if (!user.getRoles().contains(role)) {
//            user.getRoles().add(role);
//        }
        user.getRoles().add(role);

        // will automatically save without needing to call userRepo because we are using transactional
    }

    @Override
    public User getUserByUsername(String username){
        log.info("Fetching user with username: {} from database", username);
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException("User with username:" + username + " does not exist"));;
        return user;
    }


}
