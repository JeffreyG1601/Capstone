package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Zone findByName(String name);
}
