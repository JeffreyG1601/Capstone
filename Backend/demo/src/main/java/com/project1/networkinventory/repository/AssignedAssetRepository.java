package com.project1.networkinventory.repository;

import com.project1.networkinventory.model.AssignedAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignedAssetRepository extends JpaRepository<AssignedAsset, Long> {
    List<AssignedAsset> findByAsset_AssetId(Long assetId);
}
