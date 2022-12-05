package com.ahmetoral.caseproject.security;


import com.ahmetoral.caseproject.filter.CustomAuthorizationFilter;
import com.ahmetoral.caseproject.token.TokenComponent;
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

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenComponent tokenComponent;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // replaced by AuthorizationService - can be implemented if wanted
//        CustomAuthenticationFilter customAuthenticationFilter =
//                new CustomAuthenticationFilter(authenticationManagerBean(), tokenComponent, userService);
//        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login/**");

        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/v1/login/**", "/token/refresh/**", "/api/v1/register/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/users/**","/api/v1/user/**" ).hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/api/v1/weather/new/**" ).hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
//        http.addFilter(customAuthenticationFilter); // replaced by AuthorizationService - can be implemented if wanted
        http.addFilterBefore(new CustomAuthorizationFilter(tokenComponent), UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



}
