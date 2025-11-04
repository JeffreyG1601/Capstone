package com.project1.networkinventory.repository;

import com.project1.networkinventory.model.Headend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadendRepository extends JpaRepository<Headend, Long> {
}
