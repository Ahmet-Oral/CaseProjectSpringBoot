package com.ahmetoral.inventorymanagement.service;

import com.ahmetoral.inventorymanagement.model.FailedLoginAttempt;
import com.ahmetoral.inventorymanagement.model.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuthenticationService {


    ResponseEntity<Map<String,String>> authenticateUser(String username, String password, HttpServletRequest request);
    Integer increaseFailedLoginAttempt(String username);
    void resetFailedLoginAttempt(String username);
    FailedLoginAttempt getFailedLoginAttemptByUser(User user);
    void lockUserAccount(User user);

}
