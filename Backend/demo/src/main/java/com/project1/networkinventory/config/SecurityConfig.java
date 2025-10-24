package com.project1.networkinventory.config;

//Spring Boot 3 / Spring Security 6 style
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

 @Bean
 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     http
         .csrf().disable() // disable for API development (or configure token usage)
         .authorizeHttpRequests(auth -> auth
             .requestMatchers("/api/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
             .anyRequest().authenticated()
         )
         .httpBasic(Customizer.withDefaults()); // optional: enables basic auth for other routes

     return http.build();
 }
}
