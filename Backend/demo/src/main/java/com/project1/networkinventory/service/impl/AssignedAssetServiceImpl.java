package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.AssignedAsset;
import com.project1.networkinventory.repository.AssignedAssetRepository;
import com.project1.networkinventory.service.AssignedAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignedAssetServiceImpl implements AssignedAssetService {

    private final AssignedAssetRepository repository;

    // explicit constructor (keeps compatibility)
    public AssignedAssetServiceImpl(AssignedAssetRepository repository) {
        this.repository = repository;
    }

    @Override
    public AssignedAsset assignAsset(AssignedAsset assignedAsset) {
        return repository.save(assignedAsset);
    }

    @Override
    public List<AssignedAsset> getAllAssignedAssets() {
        return repository.findAll();
    }

    @Override
    public AssignedAsset getAssignedAssetById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void unassignAsset(Long id) {
        repository.deleteById(id);
    }
}
