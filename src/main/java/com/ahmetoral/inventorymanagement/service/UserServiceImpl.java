package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.exception.ApiRequestException;
import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;
import com.ahmetoral.inventorymanagement.repo.RoleRepo;
import com.ahmetoral.inventorymanagement.repo.UserRepo;
import com.ahmetoral.inventorymanagement.requests.UserRequest;
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
    public void saveUser(UserRequest userRequest) {
        log.info("Saving new user with username: {} to the database", userRequest.getUsername());
        if (userRequest.getUsername() == null || userRequest.getPassword() == null) {
            throw new ApiRequestException("Username or password can't be null");
        }        if (checkUsernameExists(userRequest.getUsername())){
            throw new ApiRequestException("User with username: " + userRequest.getUsername() + " already exist");
        }
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepo.save(user);
        log.info("role ke: {}", userRequest.getRole());
        if (userRequest.getRole().equals("")){
            setUserRole(userRequest.getUsername(),"ROLE_USER");
        } else {
            setUserRole(userRequest.getUsername(),userRequest.getRole());
        }
    }

    @Override
    public void updateUser(UserRequest userRequest) {
        log.info("updating user with id: {} to the database", userRequest.getId());
        User user = getUserById(UUID.fromString(userRequest.getId()));
        setUserRole(user.getUsername(), userRequest.getRole());
        user.setUsername(userRequest.getUsername());
        user.setLocked((userRequest.getLocked()));
        userRepo.save(user);

    }

    @Override
    public void deleteUserById(UUID id) {
        log.info("deleting user with id: " + id);
        userRepo.deleteById(id);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role: {} to the database", role.getName());
        if (role.getName().equals("")) {
            throw new ApiRequestException("Role cannot be empty");
        }
        if (checkRoleExists(role.getName())){
            throw new ApiRequestException("Role:" + role.getName() + " already exist");
        }
        return roleRepo.save(role);
    }

    @Override
    public void setUserRole(String username, String roleName) {
        log.info("Adding role: {} to user with username: {} the database", roleName, username);
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException("User with username: " + username + " does not exist"));
        // Throw exception if role does not exist
//        Role role = roleRepo.findByName(roleName)
//                .orElseThrow(() -> new ApiRequestException("Role:" + roleName + " does not exist"));

        // Create new role if role does not exist
        if (!checkRoleExists(roleName)) {
            saveRole(new Role(UUID.randomUUID(), roleName));
        }
        Role role = roleRepo.findByName(roleName).get();
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
