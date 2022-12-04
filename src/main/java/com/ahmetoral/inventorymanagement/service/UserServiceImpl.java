package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.exception.ApiRequestException;
import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;
import com.ahmetoral.inventorymanagement.repo.RoleRepo;
import com.ahmetoral.inventorymanagement.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // leaving dependency injection to Lombok,
//@Transactional
@Slf4j // logging
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading UserDetails for username: {} from database", username);

        User user = userRepo.findByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("User with username: " + username + " not found in the database"));

        log.info("user: {}", user);

        if (user.getLocked()) {
            log.error("Account with username: " + username + " is locked");
            return null;
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        // User(String username, String password, Collection<? extends GrantedAuthority > authorities)

    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users from database");
        return userRepo.findAll();
    }

    @Override
    public User getUserByUsername(String username){
        log.info("Fetching user with username: {} from database", username);
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException("User with username:" + username + " does not exist"));
        return user;
    }

    @Override
    public User getUserById(UUID id) {
        log.info("Fetching user with id: {} from database", id);
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ApiRequestException("User with id:" + id + " does not exist"));

        return user;
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user with username: {} to the database", user.getUsername());
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new ApiRequestException("Username or password can't be null");
        }        if (checkUsernameExists(user.getUsername())){
            throw new ApiRequestException("User with username: " + user.getUsername() + " already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }

    @Override
    public void updateUser(User user) {
        log.info("updating user with username: {} to the database", user.getUsername());
        userRepo.save(user);

    }

    @Override
    public void deleteUserByUsername(String username) {
        log.info("deleting user with username: " + username);
        if (username == null || !checkUsernameExists(username)) {
            throw new ApiRequestException("Can't find user with username: " + username);
        }
        userRepo.delete(getUserByUsername(username));
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role: {} to the database", role.getName());
        // todo complete role validation
        if (checkRoleExists(role.getName())){
            throw new ApiRequestException("Role:" + role.getName() + " already exist");
        }
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role: {} to user with username: {} the database", roleName, username);
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException("User with username: " + username + " does not exist"));
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new ApiRequestException("Role:" + roleName + " does not exist"));

//        if (!user.getRoles().contains(role)) {
//            user.getRoles().add(role);
//        }
        if (user.getRoles() != null) {
            user.getRoles().clear();// remove the old role
        }
        user.getRoles().add(role);

        userRepo.save(user);

    }

    @Override
    public Boolean checkUsernameExists(String username) {
        log.info("Checking if user with username: {} exists in database", username);
        return userRepo.findByUsername(username).isPresent();
    }

    @Override
    public Boolean checkRoleExists(String role) {
        log.info("Checking if role: {} exists in database", role);
        return roleRepo.findByName(role).isPresent();
    }


}
