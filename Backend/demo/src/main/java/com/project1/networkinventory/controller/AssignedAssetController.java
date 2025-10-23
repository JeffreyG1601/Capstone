package com.project1.networkinventory.controller;

import com.project1.networkinventory.model.AssignedAsset;
import com.project1.networkinventory.service.AssignedAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assigned-assets")
@RequiredArgsConstructor
public class AssignedAssetController {

    private final AssignedAssetService assignedAssetService = null;

    @PostMapping
    public AssignedAsset assignAsset(@RequestBody AssignedAsset assignedAsset) {
        return assignedAssetService.assignAsset(assignedAsset);
    }

    @GetMapping
    public List<AssignedAsset> getAllAssignedAssets() {
        return assignedAssetService.getAllAssignedAssets();
    }

    @GetMapping("/{id}")
    public AssignedAsset getAssignedAssetById(@PathVariable Long id) {
        return assignedAssetService.getAssignedAssetById(id);
    }

    @DeleteMapping("/{id}")
    public void unassignAsset(@PathVariable Long id) {
        assignedAssetService.unassignAsset(id);
    }
}
