package com.ahmetoral.inventorymanagement.security;


import com.ahmetoral.inventorymanagement.filter.CustomAuthorizationFilter;
import com.ahmetoral.inventorymanagement.service.UserService;
import com.ahmetoral.inventorymanagement.token.TokenComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // injected by RequiredArgsConstructor
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenComponent tokenComponent;
    private final UserService userService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {


//        CustomAuthenticationFilter customAuthenticationFilter =
//                new CustomAuthenticationFilter(authenticationManagerBean(), tokenComponent, userService);
//        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login/**");
        http.csrf().disable();
        http.cors();

        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/v1/login/**", "/token/refresh/**", "api/v1/register/**").permitAll();
//        http.authorizeRequests().antMatchers(GET, "/api/v1/user/**").hasAuthority("ROLE_USER");
//        http.authorizeRequests().antMatchers(GET, "/api/v1/users/**").hasAuthority("ROLE_ADMIN");
//        http.authorizeRequests().antMatchers(GET, "/api/v1/users/**").hasAuthority("ROLE_USER");
//        http.authorizeRequests().antMatchers(POST, "/api/v1/users/save/**").hasAuthority("ROLE_ADMIN");
//        http.authorizeRequests().anyRequest().authenticated();
//        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(tokenComponent), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



}
