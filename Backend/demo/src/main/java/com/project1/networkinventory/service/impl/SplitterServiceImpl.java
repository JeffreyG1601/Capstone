package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.model.Splitter;
import com.project1.networkinventory.repository.FiberDistributionHubRepository;
import com.project1.networkinventory.repository.SplitterRepository;
import com.project1.networkinventory.service.SplitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SplitterServiceImpl implements SplitterService {

    private final SplitterRepository repository;
    private final FiberDistributionHubRepository fdhRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public SplitterServiceImpl(SplitterRepository repository, FiberDistributionHubRepository fdhRepository, EntityManager entityManager) {
        this.repository = repository;
        this.fdhRepository = fdhRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Splitter createSplitter(Splitter splitter) {
        // ensure usedPorts default
        if (splitter.getUsedPorts() == null) splitter.setUsedPorts(0);

        // if client-supplied id present -> ensure it doesn't already exist
        if (splitter.getSplitterId() != null && repository.existsById(splitter.getSplitterId())) {
            throw new IllegalArgumentException("Splitter with id " + splitter.getSplitterId() + " already exists");
        }

        // if fdh id provided attach managed FDH
        if (splitter.getFiberDistributionHub() != null && splitter.getFiberDistributionHub().getId() != null) {
            Long fdhId = splitter.getFiberDistributionHub().getId();
            FiberDistributionHub f = fdhRepository.findById(fdhId)
                    .orElseThrow(() -> new IllegalArgumentException("FDH with id " + fdhId + " not found"));
            splitter.setFiberDistributionHub(f);
        }

        // Use persist to force INSERT even if id is set
        entityManager.persist(splitter);
        // optional flush to detect DB constraint errors early:
        // entityManager.flush();
        return splitter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Splitter> getAllSplitters() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Splitter> getSplittersByFDHId(Long fdhId) {
        return repository.findByFiberDistributionHub_Id(fdhId);
    }

    @Override
    @Transactional(readOnly = true)
    public Splitter getSplitterById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Splitter updateSplitter(Long id, Splitter splitter) {
        splitter.setSplitterId(id);
        if (splitter.getFiberDistributionHub() != null && splitter.getFiberDistributionHub().getId() != null) {
            Long fdhId = splitter.getFiberDistributionHub().getId();
            FiberDistributionHub f = fdhRepository.findById(fdhId)
                    .orElseThrow(() -> new IllegalArgumentException("FDH with id " + fdhId + " not found"));
            splitter.setFiberDistributionHub(f);
        }
        Splitter saved = repository.save(splitter);
        repository.flush();
        return saved;
    }

    @Override
    @Transactional
    public void deleteSplitter(Long id) {
        repository.deleteById(id);
    }
}
