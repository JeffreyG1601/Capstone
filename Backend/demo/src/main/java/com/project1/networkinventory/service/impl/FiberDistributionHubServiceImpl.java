package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.model.Headend;
import com.project1.networkinventory.repository.FiberDistributionHubRepository;
import com.project1.networkinventory.repository.HeadendRepository;
import com.project1.networkinventory.service.FiberDistributionHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FiberDistributionHubServiceImpl implements FiberDistributionHubService {

    private final FiberDistributionHubRepository repository;
    private final HeadendRepository headendRepository; // optional, used when headendId is provided

    @PersistenceContext
    private EntityManager entityManager;

    public FiberDistributionHubServiceImpl(FiberDistributionHubRepository repository,
                                           HeadendRepository headendRepository,
                                           EntityManager entityManager) {
        this.repository = repository;
        this.headendRepository = headendRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public FiberDistributionHub createFDH(FiberDistributionHub fdh) {
        // If client supplied id, ensure it doesn't already exist
        if (fdh.getId() != null && repository.existsById(fdh.getId())) {
            throw new IllegalArgumentException("FDH with id " + fdh.getId() + " already exists");
        }

        // Attach headend if provided (use managed entity)
        if (fdh.getHeadend() != null && fdh.getHeadend().getId() != null) {
            Long hid = fdh.getHeadend().getId();
            Headend h = headendRepository.findById(hid).orElseThrow(() -> new IllegalArgumentException("Headend with id " + hid + " not found"));
            fdh.setHeadend(h);
        }

        // Use persist to force INSERT even if id is set
        entityManager.persist(fdh);
        // optional flush to detect DB constraints early:
        // entityManager.flush();

        // return managed instance
        return fdh;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FiberDistributionHub> getAllFDHs() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public FiberDistributionHub getFDHById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public FiberDistributionHub updateFDH(Long id, FiberDistributionHub fdh) {
        fdh.setId(id);
        // attach headend if present
        if (fdh.getHeadend() != null && fdh.getHeadend().getId() != null) {
            Long hid = fdh.getHeadend().getId();
            Headend h = headendRepository.findById(hid).orElseThrow(() -> new IllegalArgumentException("Headend with id " + hid + " not found"));
            fdh.setHeadend(h);
        }
        // For update we want merge/save behavior
        FiberDistributionHub saved = repository.save(fdh);
        repository.flush();
        return saved;
    }

    @Override
    @Transactional
    public void deleteFDH(Long id) {
        repository.deleteById(id);
    }
}
