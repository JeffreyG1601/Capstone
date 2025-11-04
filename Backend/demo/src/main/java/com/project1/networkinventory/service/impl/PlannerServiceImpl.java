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

        if (req == null) throw new IllegalArgumentException("Request is null");
        if (req.getName() == null || req.getName().isBlank())
            throw new IllegalArgumentException("Customer name required");
        if (req.getSplitterId() == null)
            throw new IllegalArgumentException("Splitter selection required");

        // 1. Load splitter + FDH
        Splitter splitter = splitterRepository.findById(req.getSplitterId())
                .orElseThrow(() -> new IllegalArgumentException("Splitter not found"));

        FiberDistributionHub fdh = splitter.getFiberDistributionHub();
        if (fdh == null && req.getFdhId() != null) {
            fdh = fdhRepository.findById(req.getFdhId())
                    .orElseThrow(() -> new IllegalArgumentException("FDH not found"));
        } else if (fdh == null) {
            throw new IllegalArgumentException("Splitter has no FDH and none provided");
        }

        // 2. Validate assignedPort
        if (req.getAssignedPort() == null)
            throw new IllegalArgumentException("Assigned port is required");
        if (req.getAssignedPort() <= 0 || req.getAssignedPort() > splitter.getPortCapacity())
            throw new IllegalArgumentException("Assigned port out of range");

        if (customerRepository.existsBySplitter_SplitterIdAndAssignedPort(
                splitter.getSplitterId(), req.getAssignedPort())) {
            throw new IllegalStateException("Port already assigned to another customer");
        }

        // 3. Create Customer
        Customer customer = new Customer();
        customer.setName(req.getName());
        customer.setAddress(req.getAddress());
        customer.setNeighborhood(req.getNeighborhood());
        customer.setPlan(req.getPlan());
        customer.setConnectionType(req.getConnectionType());
        customer.setStatus(CustomerStatus.PENDING);
        customer.setSplitter(splitter);
        customer.setAssignedPort(req.getAssignedPort());
        customer.setCreatedAt(LocalDateTime.now());
        customer = customerRepository.save(customer);

        // 4. Assign ONT asset (only if provided AND customer has no ONT)
        if (req.getOntAssetId() != null) {
            boolean customerHasOnt = assetRepository.findByAssignedToCustomer_CustomerId(customer.getCustomerId())
                    .stream()
                    .anyMatch(a -> a.getAssetType() != null && a.getAssetType().equalsIgnoreCase("ONT"));

            if (!customerHasOnt) {
                Asset ont = assetRepository.findById(req.getOntAssetId())
                        .orElseThrow(() -> new IllegalArgumentException("ONT asset not found"));
                if (ont.getStatus() != AssetStatus.AVAILABLE)
                    throw new IllegalStateException("ONT asset is not AVAILABLE");

                ont.setAssignedToCustomer(customer);
                ont.setAssignedDate(LocalDateTime.now());
                ont.setStatus(AssetStatus.ASSIGNED);
                assetRepository.save(ont);

                AssignedAsset link = new AssignedAsset();
                link.setAsset(ont);
                link.setCustomer(customer);
                link.setAssignedOn(LocalDateTime.now());
                assignedAssetRepository.save(link);
            }
        }

        // 5. Assign Router asset (only if provided AND customer has no Router)
        if (req.getRouterAssetId() != null) {
            boolean customerHasRouter = assetRepository.findByAssignedToCustomer_CustomerId(customer.getCustomerId())
                    .stream()
                    .anyMatch(a -> a.getAssetType() != null && a.getAssetType().equalsIgnoreCase("ROUTER"));

            if (!customerHasRouter) {
                Asset router = assetRepository.findById(req.getRouterAssetId())
                        .orElseThrow(() -> new IllegalArgumentException("Router asset not found"));
                if (router.getStatus() != AssetStatus.AVAILABLE)
                    throw new IllegalStateException("Router asset is not AVAILABLE");

                router.setAssignedToCustomer(customer);
                router.setAssignedDate(LocalDateTime.now());
                router.setStatus(AssetStatus.ASSIGNED);
                assetRepository.save(router);

                AssignedAsset link2 = new AssignedAsset();
                link2.setAsset(router);
                link2.setCustomer(customer);
                link2.setAssignedOn(LocalDateTime.now());
                assignedAssetRepository.save(link2);
            }
        }

        // 6. Update splitter port usage
        splitter.setUsedPorts((splitter.getUsedPorts() == null ? 0 : splitter.getUsedPorts()) + 1);
        splitterRepository.save(splitter);

        // 7. Create Deployment Task
        DeploymentTask task = new DeploymentTask();
        task.setCustomer(customer);

        if (req.getTechnicianId() != null) {
            Technician tech = technicianRepository.findById(req.getTechnicianId())
                    .orElseThrow(() -> new IllegalArgumentException("Technician not found"));
            task.setTechnician(tech);
        }

        task.setStatus(DeploymentStatus.SCHEDULED);
        task.setScheduledDate(req.getScheduledDate() != null
                ? req.getScheduledDate().toLocalDate()
                : LocalDate.now());

        InstallationNote note = new InstallationNote();
        note.setAuthor("Planner");
        note.setNote("Created during onboarding");
        note.setTimestamp(LocalDateTime.now());
        task.addNote(note);

        deploymentTaskRepository.save(task);

        return new OnboardResponse(
                customer.getCustomerId(),
                task.getTaskId(),
                "Customer onboarded and task created successfully"
        );
    }
}
