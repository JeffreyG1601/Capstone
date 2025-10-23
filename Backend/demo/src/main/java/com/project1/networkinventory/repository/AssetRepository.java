package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.Asset;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByAssetType(String type); // corrected method name
    List<Asset> findByStatus(String status);
    List<Asset> findByAssignedToCustomer_CustomerId(Long customerId); // fixed
}
