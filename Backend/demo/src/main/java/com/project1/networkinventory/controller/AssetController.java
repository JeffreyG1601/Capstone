// com.project1.networkinventory.controller.AssetController.java
package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.AssetDTO;
import com.project1.networkinventory.mapper.AssetMapper;
import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public ResponseEntity<?> createAsset(@RequestBody AssetDTO assetDto) {
        // map to entity
        Asset asset = AssetMapper.toEntity(assetDto);

        if (asset.getSerialNumber() != null) {
            Optional<Asset> existing = assetService.findBySerialNumber(asset.getSerialNumber());
            if (existing.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Asset with serialNumber " + asset.getSerialNumber() + " already exists.");
            }
        }

        Asset created = assetService.createAsset(asset);
        return ResponseEntity.status(HttpStatus.CREATED).body(AssetMapper.toDto(created));
    }

    @GetMapping
    public ResponseEntity<List<AssetDTO>> getAllAssets() {
        List<Asset> list = assetService.getAllAssets();
        return ResponseEntity.ok(AssetMapper.toDtos(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getAssetById(@PathVariable Long id) {
        Asset a = assetService.getAssetById(id);
        return a == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(AssetMapper.toDto(a));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAsset(@PathVariable Long id, @RequestBody AssetDTO assetDto) {
        Asset asset = AssetMapper.toEntity(assetDto);

        if (asset.getSerialNumber() != null) {
            Optional<Asset> existing = assetService.findBySerialNumber(asset.getSerialNumber());
            if (existing.isPresent() && !existing.get().getAssetId().equals(id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Serial number already used by another asset.");
            }
        }

        Asset updated = assetService.updateAsset(id, asset);
        return ResponseEntity.ok(AssetMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<AssetDTO>> getAssetsByType(@PathVariable String type) {
        List<Asset> list = assetService.getAssetsByType(type);
        return ResponseEntity.ok(AssetMapper.toDtos(list));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AssetDTO>> getAssetsByStatus(@PathVariable String status) {
        // Use enum parsing tolerant method
        // If you want to accept AssetStatus path variable directly, change argument to AssetStatus and rely on @JsonCreator (but path var binding is simpler via string)
        com.project1.networkinventory.enums.AssetStatus s =
            com.project1.networkinventory.enums.AssetStatus.fromString(status);
        if (s == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Asset> list = assetService.getAssetsByStatus(s);
        return ResponseEntity.ok(AssetMapper.toDtos(list));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AssetDTO>> getAssetsByCustomer(@PathVariable Long customerId) {
        List<Asset> list = assetService.getAssetsByCustomerId(customerId);
        return ResponseEntity.ok(AssetMapper.toDtos(list));
    }

    @GetMapping("/serial/{serial}")
    public ResponseEntity<AssetDTO> getAssetBySerial(@PathVariable String serial) {
        Optional<Asset> opt = assetService.findBySerialNumber(serial);
        return opt.map(a -> ResponseEntity.ok(AssetMapper.toDto(a))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
