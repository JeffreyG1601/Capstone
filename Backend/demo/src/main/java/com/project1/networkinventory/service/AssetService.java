package com.project1.networkinventory.service;

import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.enums.AssetStatus;
import java.util.List;
import java.util.Optional;

public interface AssetService {
    Asset createAsset(Asset asset);
    List<Asset> getAllAssets();
    Asset getAssetById(Long id);
    Asset updateAsset(Long id, Asset asset);
    void deleteAsset(Long id);
    List<Asset> getAssetsByType(String type);
    List<Asset> getAssetsByStatus(AssetStatus status);
    List<Asset> getAssetsByCustomerId(Long customerId);

    Optional<Asset> findBySerialNumber(String serialNumber);
}
