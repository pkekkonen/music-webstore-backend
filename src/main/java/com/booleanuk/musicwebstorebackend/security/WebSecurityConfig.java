package com.booleanuk.musicwebstorebackend.security;

import com.booleanuk.musicwebstorebackend.security.jwt.AuthEntryPointJwt;
import com.booleanuk.musicwebstorebackend.security.jwt.AuthTokenFilter;
import com.booleanuk.musicwebstorebackend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/auth/**").permitAll()
                        //TODO: set up rules
                        //.requestMatchers(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.PUT, HttpMethod.POST, HttpMethod.GET, "/{name}/orders/{id}").access(new WebExpressionAuthorizationManager("#name == authentication.name and hasRole('USER')"))

//                        .requestMatchers(HttpMethod.GET, "/items").hasRole("USER")
//                        .requestMatchers(HttpMethod.POST, "/users/{name}/items/{itemId}").access(new WebExpressionAuthorizationManager("#name == authentication.name and hasRole('USER')"))
//                        .requestMatchers(HttpMethod.PUT, "/{name}/loans/{id}").access(new WebExpressionAuthorizationManager("#name == authentication.name and hasRole('USER')"))
//                        .requestMatchers(HttpMethod.GET, "/users/{name}/loans").access(new WebExpressionAuthorizationManager("#name == authentication.name and hasRole('USER') or hasRole('ADMIN')"))
//                        .requestMatchers("/items/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/items/{itemId}/loans").hasRole("ADMIN")
                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}