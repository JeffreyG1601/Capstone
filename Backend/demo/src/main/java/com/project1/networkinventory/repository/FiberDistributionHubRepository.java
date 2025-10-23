package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.FiberDistributionHub;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiberDistributionHubRepository extends JpaRepository<FiberDistributionHub, Long> {

    List<FiberDistributionHub> findByRegion(String region);
}