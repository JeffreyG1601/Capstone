package com.project1.networkinventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.project1.networkinventory.enums.Role;
import com.project1.networkinventory.converter.RoleConverter;
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId; // PK

    private String username;

    private String passwordHash;

    @Column(name = "user_email")
    private String userEmail; // Added to support existsByUserEmail
 // in your User entity

    @Convert(converter = RoleConverter.class)
    @Column(name = "role")
    private Role role;
    private LocalDateTime lastLogin;

    // ----- Getters and Setters -----
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
}
