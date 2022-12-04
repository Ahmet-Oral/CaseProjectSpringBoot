package com.ahmetoral.inventorymanagement.filter;

import com.ahmetoral.inventorymanagement.token.TokenComponent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter { // will intercept every request coming to the application

    private final TokenComponent tokenComponent;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // if user is trying to log in or refreshing the token, let the request go through
        log.info("getServletPath: " + request.getServletPath());
        if (request.getServletPath().contains("/api/v1/login/") || request.getServletPath().equals("/token/refresh")) {
            log.info("Trying to log in");
            log.info("response: {}",response);
            filterChain.doFilter(request,response);
        }else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            log.info("request: " + request);
            log.info("authorizationHeader: " + authorizationHeader);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { // request that carries the token will have "Bearer " prefix
                try {
                    String token = authorizationHeader.substring("Bearer ".length()); // remove bearer from string to get token
                    UsernamePasswordAuthenticationToken authenticationToken = tokenComponent.getUsernamePasswordAuthenticationToken(token);
                    log.info("authenticationToken: " + authenticationToken);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
//                    response.sendError(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }

            } else {
                log.error("Unauthorized access");
                filterChain.doFilter(request, response);
            }
        }
    }
}
