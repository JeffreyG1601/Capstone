// src/main/java/com/project1/networkinventory/controller/AuthController.java
package com.project1.networkinventory.controller;

import java.util.Map;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project1.networkinventory.dto.LoginRequest;
import com.project1.networkinventory.dto.LoginResponse;
import com.project1.networkinventory.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService svc;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService svc) { this.svc = svc; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            LoginResponse res = svc.authenticate(req);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Unhandled error during login for {}: {}", req.getEmail(), ex.getMessage(), ex);
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }
}
