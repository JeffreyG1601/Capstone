package com.project1.networkinventory.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project1.networkinventory.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT helper. Generates and validates JWTs.
 * Expects a sufficiently long secret in application.properties (jwt.secret).
 */
@Component
public class JwtUtils {

    private final Key key;
    private final long jwtExpirationMs;

    public JwtUtils(@Value("${jwt.secret}") String jwtSecret,
                    @Value("${jwt.expiration-ms}") long jwtExpirationMs) {
        if (jwtSecret == null || jwtSecret.trim().length() < 32) {
            throw new IllegalStateException("jwt.secret must be set and at least 32 characters");
        }
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(User u) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(u.getUserEmail())
                .setIssuedAt(now)
                .setExpiration(exp)
                .addClaims(Map.of(
                    "userId", u.getUserId(),
                    "username", u.getUsername(),
                    "role", u.getRole() != null ? u.getRole().name() : null
                ))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateAndGetClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
