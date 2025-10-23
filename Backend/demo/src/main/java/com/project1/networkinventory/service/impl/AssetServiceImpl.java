package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.repository.AssetRepository;
import com.project1.networkinventory.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
		super();
		this.assetRepository = assetRepository;
	}

	@Override
    public Asset createAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }

    @Override
    public Asset updateAsset(Long id, Asset asset) {
        asset.setAssetId(id);
        return assetRepository.save(asset);
    }

    @Override
    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }

    @Override
    public List<Asset> getAssetsByType(String type) {
        return assetRepository.findByAssetType(type);
    }

    @Override
    public List<Asset> getAssetsByStatus(String status) {
        return assetRepository.findByStatus(status);
    }

    @Override
    public List<Asset> getAssetsByCustomerId(Long customerId) {
        return assetRepository.findByAssignedToCustomer_CustomerId(customerId);
    }
}
