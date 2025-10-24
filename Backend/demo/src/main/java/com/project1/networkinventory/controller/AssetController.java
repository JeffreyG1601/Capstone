package com.project1.networkinventory.controller;

import com.project1.networkinventory.enums.AssetStatus;
import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")

public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
		super();
		this.assetService = assetService;
	}

	/**
     * Create a new asset. If an asset with same serialNumber exists, respond 409 Conflict.
     */
    @PostMapping
    public ResponseEntity<?> createAsset(@RequestBody Asset asset) {
        if (asset.getSerialNumber() != null) {
            Optional<Asset> existing = assetService.findBySerialNumber(asset.getSerialNumber());
            if (existing.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Asset with serialNumber " + asset.getSerialNumber() + " already exists.");
            }
        }
        Asset created = assetService.createAsset(asset);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get all assets. (Simple list endpoint per current SRS)
     */
    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        List<Asset> list = assetService.getAllAssets();
        return ResponseEntity.ok(list);
    }

    /**
     * Get asset by id, 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long id) {
        Asset a = assetService.getAssetById(id);
        return a == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(a);
    }

    /**
     * Update (replace) asset by id.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Asset asset) {
        // Optional: protect serial number uniqueness on update
        if (asset.getSerialNumber() != null) {
            Optional<Asset> existing = assetService.findBySerialNumber(asset.getSerialNumber());
            if (existing.isPresent() && !existing.get().getAssetId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        Asset updated = assetService.updateAsset(id, asset);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete asset by id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Find assets by type (e.g., "ONT", "Router").
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Asset>> getAssetsByType(@PathVariable String type) {
        List<Asset> list = assetService.getAssetsByType(type);
        return ResponseEntity.ok(list);
    }

    /**
     * Find assets by status using the AssetStatus enum (Available, Assigned, Faulty, Retired).
     * Example: GET /api/assets/status/Available
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Asset>> getAssetsByStatus(@PathVariable AssetStatus status) {
        List<Asset> list = assetService.getAssetsByStatus(status);
        return ResponseEntity.ok(list);
    }

    /**
     * Find assets assigned to a given customer id.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Asset>> getAssetsByCustomer(@PathVariable Long customerId) {
        List<Asset> list = assetService.getAssetsByCustomerId(customerId);
        return ResponseEntity.ok(list);
    }

    /**
     * Lookup asset by serial number.
     */
    @GetMapping("/serial/{serial}")
    public ResponseEntity<Asset> getAssetBySerial(@PathVariable String serial) {
        Optional<Asset> opt = assetService.findBySerialNumber(serial);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
