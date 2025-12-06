package com.daksh.xeno.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // allow everything under /api for now
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().permitAll()
                )
                // you can keep httpBasic or remove it; it won't matter if all are permitAll
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
