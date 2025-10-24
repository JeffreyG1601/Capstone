package com.project1.networkinventory.repository;

import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.enums.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByAssetType(String type);
    List<Asset> findByStatus(AssetStatus status);
    List<Asset> findByAssignedToCustomer_CustomerId(Long customerId);

    Optional<Asset> findBySerialNumber(String serialNumber);
}
