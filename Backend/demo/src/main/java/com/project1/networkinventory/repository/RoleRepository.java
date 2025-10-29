// src/main/java/com/project1/networkinventory/repository/RoleRepository.java
package com.project1.networkinventory.repository;
import com.project1.networkinventory.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}
