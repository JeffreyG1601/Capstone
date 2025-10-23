package com.project1.networkinventory.controller;

import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
		super();
		this.assetService = assetService;
	}

	@PostMapping
    public Asset createAsset(@RequestBody Asset asset) {
        return assetService.createAsset(asset);
    }

    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    @GetMapping("/{id}")
    public Asset getAssetById(@PathVariable Long id) {
        return assetService.getAssetById(id);
    }

    @PutMapping("/{id}")
    public Asset updateAsset(@PathVariable Long id, @RequestBody Asset asset) {
        return assetService.updateAsset(id, asset);
    }

    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }

    @GetMapping("/type/{type}")
    public List<Asset> getAssetsByType(@PathVariable String type) {
        return assetService.getAssetsByType(type);
    }

    @GetMapping("/status/{status}")
    public List<Asset> getAssetsByStatus(@PathVariable String status) {
        return assetService.getAssetsByStatus(status);
    }

    @GetMapping("/customer/{customerId}")
    public List<Asset> getAssetsByCustomer(@PathVariable Long customerId) {
        return assetService.getAssetsByCustomerId(customerId);
    }
}
