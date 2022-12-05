package com.ahmetoral.caseproject.service;

import com.ahmetoral.caseproject.model.FailedLoginAttempt;
import com.ahmetoral.caseproject.model.User;
import com.ahmetoral.caseproject.repo.FailedLoginAttemptRepo;
import com.ahmetoral.caseproject.repo.UserRepo;
import com.ahmetoral.caseproject.token.TokenComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor // leaving dependency injection to Lombok,
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenComponent tokenComponent;
    private final UserRepo userRepo;
    private final FailedLoginAttemptRepo failedLoginAttemptRepo;


    @Override
    public ResponseEntity<Map<String, String>> authenticateUser(String username, String password, HttpServletRequest request) {
        log.info("Login attempt - Username is: {} - Password is: {}", username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken); // will throw exception if authentication fails
            Map<String,String> map = tokenComponent.generateAndGetTokenMap(request, authentication);
            resetFailedLoginAttempt(username); // reset failed login attempt on successfulAuthentication
            return ResponseEntity.ok().body(map);
        } catch (Exception e) { // if authentication fails,
            log.info("---Authentication failed");
            if (userService.checkUsernameExists(username)) {
                //increase number of failed login attempts
                if (increaseFailedLoginAttempt(username) < 4){ // if password is incorrect
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                }
                return ResponseEntity.status(HttpStatus.LOCKED).body(null); // if account is locked
            }else { // user not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
//            throw new ApiRequestException("Authentication failed for username: " + username);
        }
    }

    @Override
    public Integer increaseFailedLoginAttempt(String username) {
        Optional<User> userOpt = userRepo.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getLocked()) { // if user is already locked
                return 99;
            }
            log.info("Increasing number of failed attempt for user with username: {}", user.getUsername());
            FailedLoginAttempt failedLoginAttempt = getFailedLoginAttemptByUser(user);
            failedLoginAttempt.setNumberOfAttempts(failedLoginAttempt.getNumberOfAttempts() + 1);
            failedLoginAttempt.setUser(user);
            failedLoginAttemptRepo.save(failedLoginAttempt);
            log.info("Number of failed attempts: {}", failedLoginAttempt.getNumberOfAttempts());
            if (failedLoginAttempt.getNumberOfAttempts() > 4) {
                lockUserAccount(user);
            }
            return failedLoginAttempt.getNumberOfAttempts();
        }
        return 0;

    }

    @Override
    public FailedLoginAttempt getFailedLoginAttemptByUser(User user) {
        Optional<FailedLoginAttempt> failedLoginAttempt = failedLoginAttemptRepo.findByUser(user);
        return failedLoginAttempt.orElse(new FailedLoginAttempt());
    }

    @Override
    public void resetFailedLoginAttempt(String username) {
        Optional<User> userOpt = userRepo.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            log.info("Resetting number of failed attempt for user with username: {}", user.getUsername());
            FailedLoginAttempt failedLoginAttempt = getFailedLoginAttemptByUser(user);
            failedLoginAttempt.setNumberOfAttempts(0);
            failedLoginAttempt.setUser(user);
            failedLoginAttemptRepo.save(failedLoginAttempt);
        }
    }

    @Override
    public void lockUserAccount(User user) {
        user.setLocked(true); // lock the account
        userRepo.save(user);
    }



}
