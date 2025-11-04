package com.project1.networkinventory.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
}
