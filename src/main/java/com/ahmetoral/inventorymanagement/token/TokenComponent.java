package com.ahmetoral.inventorymanagement.token;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface TokenComponent {
    String getAlgorithmSecret();
    Map<String, String> generateAndGetTokenMap(HttpServletRequest request, Authentication authentication);
    Map<String, String> refreshAccessToken(HttpServletRequest request, HttpServletResponse response);
    Algorithm generateAlgorithm();
    String generateAccessToken(String username, String requestUrl, Supplier<Stream<GrantedAuthority>> grantedAuthorityStream);
    String generateRefreshToken(String username, String requestUrl, Supplier<Stream<GrantedAuthority>> grantedAuthorityStream);
    UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token);



}
