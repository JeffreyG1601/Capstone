// src/main/java/com/project1/networkinventory/controller/PlannerController.java
package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.OnboardRequest;
import com.project1.networkinventory.model.*;
import com.project1.networkinventory.repository.*;
import com.project1.networkinventory.enums.DeploymentStatus;
import com.project1.networkinventory.service.PlannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {

    private final FiberDistributionHubRepository fdhRepo;
    private final SplitterRepository splitterRepo;
    private final CustomerRepository customerRepo;
    private final AssetRepository assetRepo;
    private final DeploymentTaskRepository taskRepo;

    public PlannerController(FiberDistributionHubRepository fdhRepo,
                             SplitterRepository splitterRepo,
                             CustomerRepository customerRepo,
                             AssetRepository assetRepo,
                             DeploymentTaskRepository taskRepo) {
        this.fdhRepo = fdhRepo;
        this.splitterRepo = splitterRepo;
        this.customerRepo = customerRepo;
        this.assetRepo = assetRepo;
        this.taskRepo = taskRepo;
    }

    @GetMapping("/fdhs")
    public List<FiberDistributionHub> listFdhs() {
        return fdhRepo.findAll();
    }

    @GetMapping("/fdhs/{fdhId}/splitters")
    public List<Splitter> listSplitters(@PathVariable Long fdhId) {
        return splitterRepo.findByFiberDistributionHub_Id(fdhId);
    }

    /**
     * Onboard a customer:
     * - create customer record
     * - verify splitter + free port
     * - assign ONT/router assets if serials provided
     * - create deployment task assigned to technician
     */
    @PostMapping("/onboard")
    @Transactional
    public ResponseEntity<?> onboard(@RequestBody OnboardRequest req) {
        // basic validation
        if (req.getFdhId() == null || req.getSplitterId() == null) {
            return ResponseEntity.badRequest().body("fdhId and splitterId required");
        }

        Optional<Splitter> spOpt = splitterRepo.findById(req.getSplitterId());
        if (spOpt.isEmpty()) return ResponseEntity.badRequest().body("Splitter not found");
        Splitter splitter = spOpt.get();

        // Ensure port availability
        if (splitter.getUsedPorts() >= splitter.getPortCapacity()) {
            return ResponseEntity.status(409).body("No free ports on splitter");
        }

        // create customer
        Customer c = new Customer();
        c.setName(req.getName());
        c.setAddress(req.getAddress());
        c.setNeighborhood(req.getNeighborhood());
        c.setPlan(req.getPlan());
        c.setConnectionType(req.getConnectionType());
        c.setStatus(com.project1.networkinventory.enums.CustomerStatus.PENDING);
        c.setSplitter(splitter);
        c.setAssignedPort(req.getAssignedPort());
        c.setCreatedAt(LocalDateTime.now());
        Customer saved = customerRepo.save(c);

        // increment used ports on splitter
        splitter.setUsedPorts(splitter.getUsedPorts() + 1);
        splitterRepo.save(splitter);

        // assign assets if serials provided (optional)
        if (req.getOntAssetId() != null) {
            Optional<Asset> ontOpt = assetRepo.findById(req.getOntAssetId());
            ontOpt.ifPresent(a -> { a.setAssignedToCustomer(saved); a.setStatus(com.project1.networkinventory.enums.AssetStatus.ASSIGNED); assetRepo.save(a); });
        }
        if (req.getRouterAssetId() != null) {
            Optional<Asset> rOpt = assetRepo.findById(req.getRouterAssetId());
            rOpt.ifPresent(a -> { a.setAssignedToCustomer(saved); a.setStatus(com.project1.networkinventory.enums.AssetStatus.ASSIGNED); assetRepo.save(a); });
        }

        // create deployment task
        DeploymentTask t = new DeploymentTask();
        t.setTitle("Onboard - " + saved.getName());
        t.setCustomer(saved);
        if (req.getTechnicianId() != null) {
            Technician tech = new Technician();
            tech.setTechnicianId(req.getTechnicianId());
            t.setTechnician(tech); // set only id to avoid loading
        }
        t.setScheduledDate(req.getScheduledDate());
        t.setStatus(DeploymentStatus.SCHEDULED);
        t.setCreatedAt(LocalDateTime.now());
        DeploymentTask savedTask = taskRepo.save(t);

        // response minimal
        return ResponseEntity.ok(
                new java.util.HashMap<String, Object>() {{
                    put("customerId", saved.getCustomerId());
                    put("deploymentTaskId", savedTask.getTaskId());
                    put("message", "Onboard created and task scheduled");
                }}
        );
    }
}
