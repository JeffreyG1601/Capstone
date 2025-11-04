package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.model.Headend;
import com.project1.networkinventory.repository.FiberDistributionHubRepository;
import com.project1.networkinventory.repository.HeadendRepository;
import com.project1.networkinventory.service.HeadendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeadendServiceImpl implements HeadendService {

    private final HeadendRepository headendRepository;
    private final FiberDistributionHubRepository fdhRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public HeadendServiceImpl(HeadendRepository headendRepository, FiberDistributionHubRepository fdhRepository, EntityManager entityManager) {
        this.headendRepository = headendRepository;
        this.fdhRepository = fdhRepository;
        this.entityManager = entityManager;
    }

    /**
     * Create a Headend.
     * If incoming headend.fdhList contains FDHs with ids -> fetch managed FDH entities and attach them.
     * If FDH objects in the list have no id -> assume new and set headend reference so cascade works.
     *
     * IMPORTANT: uses EntityManager.persist() to force INSERT even when client supplies id.
     */
    @Override
    @Transactional
    public Headend createHeadend(Headend headend) {
        // If client supplied id, ensure it doesn't already exist
        if (headend.getId() != null && headendRepository.existsById(headend.getId())) {
            throw new IllegalArgumentException("Headend with id " + headend.getId() + " already exists");
        }

        // Process FDH list to attach managed entities or set headend reference for new ones
        List<FiberDistributionHub> incoming = headend.getFdhList();
        if (incoming != null && !incoming.isEmpty()) {
            List<FiberDistributionHub> attached = new ArrayList<>();
            for (FiberDistributionHub f : incoming) {
                if (f == null) continue;
                if (f.getId() != null) {
                    // load managed FDH from DB
                    Optional<FiberDistributionHub> existing = fdhRepository.findById(f.getId());
                    if (existing.isPresent()) {
                        FiberDistributionHub managed = existing.get();
                        // update basic fields (optional)
                        managed.setName(f.getName());
                        managed.setLocation(f.getLocation());
                        managed.setRegion(f.getRegion());
                        managed.setMaxPorts(f.getMaxPorts());
                        managed.setHeadend(headend);
                        attached.add(managed);
                    } else {
                        // incoming FDH id not found -> treat as new entity (assign headend)
                        f.setHeadend(headend);
                        attached.add(f);
                    }
                } else {
                    // new FDH -> set headend and let cascade/persist handle it
                    f.setHeadend(headend);
                    attached.add(f);
                }
            }
            headend.setFdhList(attached);
        }

        // Use EntityManager.persist so JPA issues INSERT (even if ID is set)
        entityManager.persist(headend);
        // optional flush to detect DB constraints early:
        // entityManager.flush();

        // Return managed instance (headend is now managed within this tx)
        return headend;
    }

    @Override
    @Transactional(readOnly = true)
    public Headend getHeadendById(Long id) {
        return headendRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Headend updateHeadend(Long id, Headend headend) {
        headend.setId(id);
        // same attach logic as create
        List<FiberDistributionHub> incoming = headend.getFdhList();
        if (incoming != null && !incoming.isEmpty()) {
            List<FiberDistributionHub> attached = new ArrayList<>();
            for (FiberDistributionHub f : incoming) {
                if (f == null) continue;
                if (f.getId() != null) {
                    FiberDistributionHub managed = fdhRepository.findById(f.getId())
                        .orElseThrow(() -> new IllegalArgumentException("FDH not found: " + f.getId()));
                    managed.setName(f.getName());
                    managed.setLocation(f.getLocation());
                    managed.setRegion(f.getRegion());
                    managed.setMaxPorts(f.getMaxPorts());
                    managed.setHeadend(headend);
                    attached.add(managed);
                } else {
                    f.setHeadend(headend);
                    attached.add(f);
                }
            }
            headend.setFdhList(attached);
        }
        // For updates, repository.save is fine (we want a merge/update)
        Headend saved = headendRepository.save(headend);
        headendRepository.flush();
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Headend> getAllHeadends() {
        return headendRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteHeadend(Long id) {
        headendRepository.deleteById(id);
    }
}
