package com.ahmetoral.inventorymanagement.controller;

import com.ahmetoral.inventorymanagement.dto.UserDto;
import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;
import com.ahmetoral.inventorymanagement.service.AuthenticationService;
import com.ahmetoral.inventorymanagement.service.UserService;
import com.ahmetoral.inventorymanagement.token.TokenComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final UserService userService;
    private final TokenComponent tokenComponent;
    private final AuthenticationService authenticationService;



    @PostMapping("/login/{username}/{password}")
    ResponseEntity<Map<String,String>> login(@PathVariable("username") String username, @PathVariable("password") String password, HttpServletRequest request) {
        return authenticationService.authenticateUser(username, password, request);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        // response 200
        return ResponseEntity.ok().body(userService.getUsers());
    }



    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        log.info("getting user with id: " + id);
        UserDto userDto = new UserDto(userService.getUserById(UUID.fromString(id)));

        // response 200
        return ResponseEntity.ok().body(userDto);
    }

    @PutMapping("/user/update/{username}/{role}/{locked}/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("username") String username, @PathVariable("role") String role, @PathVariable("locked") String locked, @PathVariable("id") String id) {
        log.info("Updating user with id: " + id);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/user/update").toUriString());


        User user = userService.getUserById(UUID.fromString(id));
        userService.addRoleToUser(user.getUsername(), role);
        user.setUsername(username);
        user.setLocked((locked.equals("true")));
        userService.updateUser(user);
        // response 201
        return ResponseEntity.created(uri).body("User updated successfully");
    }

    @DeleteMapping("/user/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable("username") String username) {
        log.info("Deleting user with username " + username);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/user/delete").toUriString());

        userService.deleteUserByUsername(username);

        // response 201
        return ResponseEntity.created(uri).body("User deleted successfully");
    }

    @PostMapping("/user/create/{username}/{password}/{role}")
    public ResponseEntity<String> createUser(@PathVariable("username") String username, @PathVariable("password") String password, @PathVariable("role") String role) {
        log.info("Creating new user with username " + username);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/user/create").toUriString());

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userService.saveUser(newUser);
        // If role does not exist, create a new role
        if (!userService.checkRoleExists(role)) {
            Role newRole = new Role();
            newRole.setName(role);
            userService.saveRole(newRole);
        }
        userService.addRoleToUser(newUser.getUsername(), role);

        // created - response 201
        return ResponseEntity.created(uri).body("user successfully created");
    }
    @PostMapping("/register/{username}/{password}")
    public ResponseEntity<String> register(@PathVariable("username") String username, @PathVariable("password") String password) {
        log.info("Registering new user with username " + username);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/register").toUriString());

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userService.saveUser(newUser);
        userService.addRoleToUser(newUser.getUsername(), "ROLE_USER");

        // response 201
        return ResponseEntity.created(uri).body("user successfully created");
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        // response 201
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        // response 200
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokenComponent.refreshAccessToken(request, response));
    }



}


// for method addRoleToUser
@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}