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

	// Default route used by frontend: /api/assets/helper?type=ONT
    @GetMapping
    public ResponseEntity<List<Asset>> listAvailableAlias(@RequestParam(name = "type") String type) {
        return listAvailable(type);
    }

    // Alternate explicit route: /api/assets/helper/available?type=ONT
    @GetMapping("/available")
    public ResponseEntity<List<Asset>> listAvailable(@RequestParam String type) {
        List<Asset> byType = assetRepository.findByAssetType(type);
        List<Asset> available = byType.stream()
                .filter(a -> a.getStatus() == AssetStatus.AVAILABLE)
                .toList();
        return ResponseEntity.ok(available);
    }
}
