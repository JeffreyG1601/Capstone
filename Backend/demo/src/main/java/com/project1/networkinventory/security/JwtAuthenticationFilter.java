package com.project1.networkinventory.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import com.project1.networkinventory.repository.UserRepository;
import com.project1.networkinventory.model.User;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtUtils.validateAndGetClaims(token);
                String email = claims.getSubject();
                Optional<User> uOpt = userRepository.findByUserEmail(email);
                if (uOpt.isPresent()) {
                    User u = uOpt.get();
                    String roleName = u.getRole().name();
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
                    var auth = new UsernamePasswordAuthenticationToken(u, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception ex) {
                // invalid token -> do nothing and let security handle unauthorized
            }
        }
        chain.doFilter(request, response);
    }
}
