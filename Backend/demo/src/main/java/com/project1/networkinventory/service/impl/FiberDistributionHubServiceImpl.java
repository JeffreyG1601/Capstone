package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.repository.FiberDistributionHubRepository;
import com.project1.networkinventory.service.FiberDistributionHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FiberDistributionHubServiceImpl implements FiberDistributionHubService {

    private final FiberDistributionHubRepository repository = null;

    @Override
    public FiberDistributionHub createFDH(FiberDistributionHub fdh) {
        return repository.save(fdh);
    }

    @Override
    public List<FiberDistributionHub> getAllFDHs() {
        return repository.findAll();
    }

    @Override
    public FiberDistributionHub getFDHById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public FiberDistributionHub updateFDH(Long id, FiberDistributionHub fdh) {
        fdh.setId(id);
        return repository.save(fdh);
    }

    @Override
    public void deleteFDH(Long id) {
        repository.deleteById(id);
    }
}
