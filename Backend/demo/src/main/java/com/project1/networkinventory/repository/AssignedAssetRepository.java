package com.project1.networkinventory.repository;

import com.project1.networkinventory.model.AssignedAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignedAssetRepository extends JpaRepository<AssignedAsset, Long> {
}
