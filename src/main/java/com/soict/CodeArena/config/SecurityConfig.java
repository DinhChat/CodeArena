package com.soict.CodeArena.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter)
            throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        // Problem endpoints - ADMIN can create/update/delete, all authenticated users
                        // can view
                        .requestMatchers("/api/problems/**").hasAnyAuthority("ADMIN", "USER", "MANAGER")
                        // Testcase endpoints - Only ADMIN can manage testcases
                        .requestMatchers("/api/testcases/**").hasAnyAuthority("ADMIN", "MANAGER")
                        // Submission endpoints - All authenticated users can submit
                        .requestMatchers("/api/submissions/**").hasAnyAuthority("ADMIN", "USER", "MANAGER")
                        .requestMatchers("/api/admins").hasAuthority("MANAGER")
                        .requestMatchers("/manager/**").hasAnyAuthority("MANAGER")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
