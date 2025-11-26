package com.Tangana.microserviciosTangana.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // para poder usar POST/PUT desde Postman sin CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // deja libres tus endpoints /api/...
                .anyRequest().permitAll()               // y el resto también, por ahora
            );

        return http.build();
    }
}
