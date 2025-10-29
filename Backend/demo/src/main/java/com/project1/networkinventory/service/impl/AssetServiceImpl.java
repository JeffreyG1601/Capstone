package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.model.Customer;
import com.project1.networkinventory.repository.AssetRepository;
import com.project1.networkinventory.repository.CustomerRepository;
import com.project1.networkinventory.service.AssetService;
import com.project1.networkinventory.enums.AssetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final CustomerRepository customerRepository;

    // Constructor if you prefer explicit
    public AssetServiceImpl(AssetRepository assetRepository, CustomerRepository customerRepository) {
        this.assetRepository = assetRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Create an asset.
     * Enforces serial-number uniqueness.
     * If asset.assignedToCustomer contains only an id, attach the managed Customer entity.
     */
    @Override
    @Transactional
    public Asset createAsset(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must not be null");
        }

        // Enforce serial uniqueness
        if (asset.getSerialNumber() != null && assetRepository.findBySerialNumber(asset.getSerialNumber()).isPresent()) {
            throw new DataIntegrityViolationException("Asset with serialNumber " + asset.getSerialNumber() + " already exists");
        }

        // If provided assignedToCustomer only has an ID, load full entity and attach
        attachExistingCustomerIfPresent(asset);

        return assetRepository.save(asset);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }

    /**
     * Update (replace) asset by id. We perform a save after setting the id.
     * Enforces serial-number uniqueness (can't change to serial already used by other asset).
     */
    @Override
    @Transactional
    public Asset updateAsset(Long id, Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must not be null");
        }

        // Check existence of target entity (optional - you may prefer to create if not exists)
        if (!assetRepository.existsById(id)) {
            throw new IllegalArgumentException("Asset with id " + id + " does not exist");
        }

        // Serial uniqueness check: if serial provided, and some other asset has that serial -> conflict
        if (asset.getSerialNumber() != null) {
            Optional<Asset> existing = assetRepository.findBySerialNumber(asset.getSerialNumber());
            if (existing.isPresent() && !existing.get().getAssetId().equals(id)) {
                throw new DataIntegrityViolationException("Serial number already used by another asset");
            }
        }

        // Attach customer entity if only id provided
        attachExistingCustomerIfPresent(asset);

        asset.setAssetId(id);
        return assetRepository.save(asset);
    }

    @Override
    @Transactional
    public void deleteAsset(Long id) {
        if (!assetRepository.existsById(id)) {
            // silent no-op or throw, choose silent no-op to match typical REST delete semantics
            return;
        }
        assetRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asset> getAssetsByType(String type) {
        return assetRepository.findByAssetType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asset> getAssetsByStatus(AssetStatus status) {
        return assetRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asset> getAssetsByCustomerId(Long customerId) {
        return assetRepository.findByAssignedToCustomer_CustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Asset> findBySerialNumber(String serialNumber) {
        return assetRepository.findBySerialNumber(serialNumber);
    }

    /**
     * Helper: if asset.assignedToCustomer != null and has a customerId, load the managed Customer
     * and set it on the asset. If id is present but not found, throw IllegalArgumentException.
     * If assignedToCustomer is null or has no id, leave as-is (null).
     */
    private void attachExistingCustomerIfPresent(Asset asset) {
        if (asset.getAssignedToCustomer() != null && asset.getAssignedToCustomer().getCustomerId() != null) {
            Long cid = asset.getAssignedToCustomer().getCustomerId();
            Customer existingCustomer = customerRepository.findById(cid)
                    .orElseThrow(() -> new IllegalArgumentException("Customer with id " + cid + " not found"));
            asset.setAssignedToCustomer(existingCustomer);
        }
    }
}
