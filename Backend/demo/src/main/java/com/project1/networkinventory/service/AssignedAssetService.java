package com.project1.networkinventory.service;

import com.project1.networkinventory.model.AssignedAsset;
import java.util.List;

public interface AssignedAssetService {
    AssignedAsset assignAsset(AssignedAsset assignedAsset);
    List<AssignedAsset> getAllAssignedAssets();
    AssignedAsset getAssignedAssetById(Long id);
    void unassignAsset(Long id);
}
