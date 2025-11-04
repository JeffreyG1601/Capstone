package com.project1.networkinventory.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project1.networkinventory.model.User;
import com.project1.networkinventory.enums.Role;
import com.project1.networkinventory.repository.UserRepository;

import java.util.Optional;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "admin@demo";
            Optional<User> existing = userRepository.findByUserEmail(adminEmail);
            if (existing.isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setUserEmail(adminEmail);
                admin.setPasswordHash(encoder.encode("password"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Seeded admin user: admin@demo / password");
            }
        };
    }
}
