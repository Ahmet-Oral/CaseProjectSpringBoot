package com.ahmetoral.caseproject.controller;

import com.ahmetoral.caseproject.dto.UserDto;
import com.ahmetoral.caseproject.model.Role;
import com.ahmetoral.caseproject.model.User;
import com.ahmetoral.caseproject.requests.UserRequest;
import com.ahmetoral.caseproject.service.AuthenticationService;
import com.ahmetoral.caseproject.service.UserService;
import com.ahmetoral.caseproject.token.TokenComponent;
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



    @PostMapping("/login")
    ResponseEntity<Map<String,String>> login(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        return authenticationService.authenticateUser(userRequest.getUsername(), userRequest.getPassword(), request);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {
        log.info("Registering user: {}" , userRequest);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/register").toUriString());
        // it should be null by default and will be automatically set to ROLE_USER but force it to for security
        userRequest.setRole("ROLE_USER");
        userService.saveUser(userRequest);
        // response 201
        return ResponseEntity.created(uri).body("user successfully created");
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

    @PutMapping("/user/update")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest userRequest) {
        log.info("Updating user - UserRequest: " + userRequest);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/user/update").toUriString());
        userService.updateUser(userRequest);
        // response 201
        return ResponseEntity.created(uri).body("User updated successfully");
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        log.info("Deleting user with id: " + id);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/user/delete").toUriString());
        userService.deleteUserById(UUID.fromString(id));
        // response 201
        return ResponseEntity.created(uri).body("User successfully deleted ");
    }

    @PostMapping("/user/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest ) {
        log.info("Creating user: {}" , userRequest);
        userService.saveUser(userRequest);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/user/create").toUriString());
        // created - response 201
        return ResponseEntity.created(uri).body("User successfully created");
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
        userService.setUserRole(form.getUsername(), form.getRoleName());
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