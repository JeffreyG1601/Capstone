package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.dto.OnboardRequest;
import com.project1.networkinventory.dto.OnboardResponse;
import com.project1.networkinventory.enums.AssetStatus;
import com.project1.networkinventory.enums.CustomerStatus;
import com.project1.networkinventory.enums.DeploymentStatus;
import com.project1.networkinventory.model.*;
import com.project1.networkinventory.repository.*;
import com.project1.networkinventory.service.PlannerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlannerServiceImpl implements PlannerService {

    private final CustomerRepository customerRepository;
    private final SplitterRepository splitterRepository;
    private final FiberDistributionHubRepository fdhRepository;
    private final AssetRepository assetRepository;
    private final AssignedAssetRepository assignedAssetRepository;
    private final DeploymentTaskRepository deploymentTaskRepository;
    private final TechnicianRepository technicianRepository;

    public PlannerServiceImpl(CustomerRepository customerRepository, SplitterRepository splitterRepository,
			FiberDistributionHubRepository fdhRepository, AssetRepository assetRepository,
			AssignedAssetRepository assignedAssetRepository, DeploymentTaskRepository deploymentTaskRepository,
			TechnicianRepository technicianRepository) {
		super();
		this.customerRepository = customerRepository;
		this.splitterRepository = splitterRepository;
		this.fdhRepository = fdhRepository;
		this.assetRepository = assetRepository;
		this.assignedAssetRepository = assignedAssetRepository;
		this.deploymentTaskRepository = deploymentTaskRepository;
		this.technicianRepository = technicianRepository;
	}

	@Override
    @Transactional
    public OnboardResponse onboardCustomer(OnboardRequest req) throws Exception {
        // Basic validation
        if (req == null) throw new IllegalArgumentException("Request is null");
        if (req.name == null || req.name.isBlank()) throw new IllegalArgumentException("Customer name required");
        if (req.splitterId == null) throw new IllegalArgumentException("Splitter selection required");

        // 1) Load splitter and FDH
        Splitter splitter = splitterRepository.findById(req.splitterId)
                .orElseThrow(() -> new IllegalArgumentException("Splitter not found"));
        FiberDistributionHub fdh = splitter.getFiberDistributionHub();
        if (fdh == null) {
            if (req.fdhId != null) {
                fdh = fdhRepository.findById(req.fdhId).orElseThrow(() -> new IllegalArgumentException("FDH not found"));
            } else {
                throw new IllegalArgumentException("Splitter has no FDH and none provided");
            }
        }

        // 2) Check assignedPort vs capacity
        if (req.assignedPort == null) throw new IllegalArgumentException("Assigned port is required");
        if (req.assignedPort <= 0 || req.assignedPort > splitter.getPortCapacity()) {
            throw new IllegalArgumentException("Assigned port is out of range for this splitter");
        }

        // ensure port not already used by a customer - check Customer table for same splitter/port
        boolean portInUse = customerRepository.findAll().stream()
                .anyMatch(c -> c.getSplitter() != null
                        && c.getSplitter().getSplitterId().equals(splitter.getSplitterId())
                        && c.getAssignedPort() != null
                        && c.getAssignedPort().equals(req.assignedPort));
        if (portInUse) {
            throw new IllegalStateException("Port already assigned to another customer");
        }

        // 3) Create Customer (status Pending -> will become Ready after assignment)
        Customer customer = new Customer();
        customer.setName(req.name);
        customer.setAddress(req.address);
        customer.setNeighborhood(req.neighborhood);
        customer.setPlan(req.plan);
        // connectionType string to enum mapping if needed; currently stored as ConnectionType enum on Customer
        if (req.connectionType != null) {
            try {
                customer.setConnectionType(com.project1.networkinventory.enums.ConnectionType.valueOf(req.connectionType));
            } catch (Exception ex) {
                // ignore or keep null
            }
        }
        customer.setStatus(CustomerStatus.Pending);
        customer.setSplitter(splitter);
        customer.setAssignedPort(req.assignedPort);
        customer.setCreatedAt(LocalDateTime.now());
        customer = customerRepository.save(customer);

        // 4) Assign assets (ONT, Router) if provided — must be Available
        if (req.ontAssetId != null) {
            Asset ont = assetRepository.findById(req.ontAssetId)
                    .orElseThrow(() -> new IllegalArgumentException("ONT asset not found"));
            if (!AssetStatus.Available.equals(ont.getStatus())) {
                throw new IllegalStateException("ONT asset is not Available");
            }
            // assign
            ont.setAssignedToCustomer(customer);
            ont.setAssignedDate(LocalDateTime.now());
            ont.setStatus(AssetStatus.Assigned);
            assetRepository.save(ont);

            AssignedAsset aa = new AssignedAsset();
            aa.setAsset(ont);
            aa.setCustomer(customer);
            aa.setAssignedOn(LocalDateTime.now());
            assignedAssetRepository.save(aa);
        }

        if (req.routerAssetId != null) {
            Asset router = assetRepository.findById(req.routerAssetId)
                    .orElseThrow(() -> new IllegalArgumentException("Router asset not found"));
            if (!AssetStatus.Available.equals(router.getStatus())) {
                throw new IllegalStateException("Router asset is not Available");
            }
            router.setAssignedToCustomer(customer);
            router.setAssignedDate(LocalDateTime.now());
            router.setStatus(AssetStatus.Assigned);
            assetRepository.save(router);

            AssignedAsset aa2 = new AssignedAsset();
            aa2.setAsset(router);
            aa2.setCustomer(customer);
            aa2.setAssignedOn(LocalDateTime.now());
            assignedAssetRepository.save(aa2);
        }

        // 5) increment splitter.usedPorts (reflect capacity usage)
        Integer used = splitter.getUsedPorts();
        if (used == null) used = 0;
        splitter.setUsedPorts(used + 1);
        splitterRepository.save(splitter);

        // 6) Create DeploymentTask and assign to technician if provided
        DeploymentTask task = new DeploymentTask();
        task.setCustomer(customer);
        if (req.technicianId != null) {
            Technician tech = technicianRepository.findById(req.technicianId)
                    .orElseThrow(() -> new IllegalArgumentException("Technician not found"));
            task.setTechnician(tech);
        }
        task.setStatus(DeploymentStatus.Scheduled);
        task.setScheduledDate(req.scheduledDate != null ? req.scheduledDate : LocalDate.now());
        task.setNotes("Created by Planner during onboarding");
        task = deploymentTaskRepository.save(task);

        // 7) Mark customer Active/Ready for deployment - depends on your process; we set Pending -> Active/ReadyForDeployment
        customer.setStatus(CustomerStatus.Active);
        customerRepository.save(customer);

        return new OnboardResponse(customer.getCustomerId(), task.getTaskId(), "Customer onboarded and task created");
    }
}
