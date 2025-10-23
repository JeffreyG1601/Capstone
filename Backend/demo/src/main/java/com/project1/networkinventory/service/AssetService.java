package com.project1.networkinventory.service;

import com.project1.networkinventory.model.Asset;
import java.util.List;

public interface AssetService {
    Asset createAsset(Asset asset);
    List<Asset> getAllAssets();
    Asset getAssetById(Long id);
    Asset updateAsset(Long id, Asset asset);
    void deleteAsset(Long id);
    List<Asset> getAssetsByType(String type);
    List<Asset> getAssetsByStatus(String status); // optionally use enum
    List<Asset> getAssetsByCustomerId(Long customerId);
}
