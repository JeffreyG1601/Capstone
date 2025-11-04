package com.project1.networkinventory.service;

import com.project1.networkinventory.dto.LoginRequest;
import com.project1.networkinventory.dto.LoginResponse;

/**
 * Authentication service API.
 * Implementations perform credential checks and token generation.
 */
public interface AuthService {
    /**
     * Authenticate credentials and return login response containing JWT token and user DTO.
     * @param req login request (email + password)
     * @return LoginResponse with token and user
     * @throws IllegalArgumentException on bad credentials
     */
    LoginResponse authenticate(LoginRequest req);
}
