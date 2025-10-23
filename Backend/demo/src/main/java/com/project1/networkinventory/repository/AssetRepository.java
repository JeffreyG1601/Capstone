package com.project1.networkinventory.repository;

import com.project1.networkinventory.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByAssetType(String type);
    List<Asset> findByStatus(String status); // if using enum, change type to AssetStatus
    List<Asset> findByAssignedToCustomer_CustomerId(Long customerId);
}
