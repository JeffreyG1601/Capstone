package com.project1.networkinventory.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project1.networkinventory.dto.LoginRequest;
import com.project1.networkinventory.dto.LoginResponse;
import com.project1.networkinventory.model.User;
import com.project1.networkinventory.repository.UserRepository;
import com.project1.networkinventory.security.JwtUtils;
import com.project1.networkinventory.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    @Transactional
    public LoginResponse authenticate(LoginRequest req) {
        Optional<User> uOpt = userRepository.findByUserEmail(req.getEmail());
        if (uOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        User u = uOpt.get();
        if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        // update last login timestamp
        u.setLastLogin(LocalDateTime.now());
        userRepository.save(u);

        String token = jwtUtils.generateToken(u);
        return new LoginResponse(token, u);
    }
}
