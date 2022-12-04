package com.ahmetoral.inventorymanagement.controller;

import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;
import com.ahmetoral.inventorymanagement.service.UserService;
import com.ahmetoral.inventorymanagement.token.TokenComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final UserService userService;
    private final TokenComponent tokenComponent;
    private final AuthenticationManager authenticationManager;



    @PostMapping("/login/{username}/{password}")
    ResponseEntity<Map<String,String>> login(@PathVariable("username") String username, @PathVariable("password") String password, HttpServletRequest request) {
        log.info("---Username is: {}", username); log.info("--Password is: {}", password);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.info("---authenticationToken is: {}", authenticationToken);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return ResponseEntity.ok().body(tokenComponent.generateAndGetTokenMap(request, authentication));
        } catch (Exception e) {
            log.info("---Authentication failed");
            userService.newFailedLoginAttempt(username);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return ResponseEntity.ok().body(tokenComponent.generateAndGetTokenMap(request, authentication));
        }



    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        // response 200
        return ResponseEntity.ok().body(userService.getUsers());
    }


    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        // response 200
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/user/save").toUriString());

        // response 201
        return ResponseEntity.created(uri).body(userService.saveUser(user));
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