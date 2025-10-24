package com.project1.networkinventory.controller;

import com.project1.networkinventory.enums.AssetStatus;
import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets/helper")
@RequiredArgsConstructor
public class AssetsHelperController {
    private final AssetRepository assetRepository;

    public AssetsHelperController(AssetRepository assetRepository) {
		super();
		this.assetRepository = assetRepository;
	}

	// GET /api/assets/helper/available?type=ONT
    @GetMapping("/available")
    public ResponseEntity<List<Asset>> listAvailable(@RequestParam String type) {
        List<Asset> byType = assetRepository.findByAssetType(type);
        // filter by status Available
        List<Asset> available = byType.stream()
                .filter(a -> a.getStatus() != null && a.getStatus().equals(AssetStatus.Available))
                .toList();
        return ResponseEntity.ok(available);
    }
}
