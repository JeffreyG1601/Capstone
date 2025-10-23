package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByUsername(String username);

    // Match the actual field name in User entity
    boolean existsByUserEmail(String userEmail); // Make sure User entity has 'userEmail'
}
