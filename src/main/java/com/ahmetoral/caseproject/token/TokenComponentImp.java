package com.ahmetoral.caseproject.token;

import com.ahmetoral.caseproject.model.Role;
import com.ahmetoral.caseproject.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

// todo optimize
@Slf4j
@Component
public class TokenComponentImp implements TokenComponent {

    private final UserService userService;

    public TokenComponentImp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getAlgorithmSecret() {
        // todo - secret string is only exposed in development, secure it using something like utility class before switching to production
        return "superSecret";
    }

    @Override
    public String generateAccessToken(String username, String requestUrl, Supplier<Stream<GrantedAuthority>> grantedAuthorityStreamSupplier) {
        return  JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 10000)) // 10 minutes
                .withIssuer(requestUrl)
                .withClaim("roles", grantedAuthorityStreamSupplier.get()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(generateAlgorithm());
    }

    @Override
    public String generateRefreshToken(String username, String requestUrl, Supplier<Stream<GrantedAuthority>> grantedAuthorityStreamSupplier) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 10000)) // 60 minutes
                .withIssuer(requestUrl)
                .withClaim("roles", grantedAuthorityStreamSupplier.get()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(generateAlgorithm());        }

    @Override
    public Map<String, String> generateAndGetTokenMap(HttpServletRequest request, Authentication authentication) {
        // org.springframework.security.core.userdetails
        User user = (User) authentication.getPrincipal();
        log.info("User with username: {} authenticated",user.getUsername());

        String username = user.getUsername();
        String requestUrl = request.getRequestURL().toString();
        Supplier<Stream<GrantedAuthority>> grantedAuthorityStreamSupplier
                = () -> user.getAuthorities().stream();

        String accessToken = generateAccessToken(username, requestUrl, grantedAuthorityStreamSupplier);
        String refreshToken = generateRefreshToken(username, requestUrl, grantedAuthorityStreamSupplier);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        tokens.put("username", username);
        if (grantedAuthorityStreamSupplier.get().findFirst().isPresent()){ // if user has role
            tokens.put("role", String.valueOf(grantedAuthorityStreamSupplier.get().findFirst().get()));
        } else { // if user has no role
            tokens.put("role", "");
        }
        return tokens;
    }


    @Override
    public Map<String, String>  refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { // request that carries the token will have "Bearer " prefix
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length()); // remove bearer from string to get token
                JWTVerifier verifier = JWT.require(generateAlgorithm()).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                String username = decodedJWT.getSubject();
                com.ahmetoral.caseproject.model.User user = userService.getUserByUsername(username);
                String requestUrl = request.getRequestURL().toString();

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 10000)) // 10 minutes
                        .withIssuer(requestUrl)
                        .withClaim("roles", user.getRoles().stream()
                                .map(Role::getName).collect(Collectors.toList()))
                        .sign(generateAlgorithm());

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                return tokens;
            } catch (Exception exception) {
                log.error("Error logging in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                return error;
            }
        } else {
            throw new RuntimeException("Unauthorized access");
        }
    }

    @Override
    public Algorithm generateAlgorithm() {
        return Algorithm.HMAC256(getAlgorithmSecret().getBytes());
    }



    @Override
    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {
        JWTVerifier verifier = JWT.require(generateAlgorithm()).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> { // convert String roles to SimpleGrantedAuthority
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return new UsernamePasswordAuthenticationToken(username,null, authorities);
    }
}
