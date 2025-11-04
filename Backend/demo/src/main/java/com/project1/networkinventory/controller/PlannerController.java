package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.OnboardRequest;
import com.project1.networkinventory.dto.OnboardResponse;
import com.project1.networkinventory.enums.CustomerStatus;
import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.model.Customer;
import com.project1.networkinventory.repository.AssetRepository;
import com.project1.networkinventory.repository.CustomerRepository;
import com.project1.networkinventory.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/planner")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PlannerController {

    private final PlannerService plannerService;
    private final CustomerRepository customerRepository;
    private final AssetRepository assetRepository;

    public PlannerController(PlannerService plannerService, CustomerRepository customerRepository, AssetRepository assetRepository) {
        super();
        this.plannerService = plannerService;
        this.customerRepository = customerRepository;
        this.assetRepository = assetRepository;
    }

    // Main onboarding endpoint (controller is NOT transactional)
    @PostMapping("/onboard")
    public ResponseEntity<?> onboard(@RequestBody OnboardRequest req) {
        try {
            OnboardResponse resp = plannerService.onboardCustomer(req);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Onboard failed: " + e.getMessage());
        }
    }

    /**
     * GET /api/planner/pending
     * Returns customers that should appear in "Pending":
     *  - status == PENDING
     *  - OR missing required fields (name blank/null, splitter null, assignedPort null)
     *  - OR missing ONT or Router assignment (even if status != PENDING)
     */
    @GetMapping("/pending")
    public ResponseEntity<List<Map<String,Object>>> listPendingCustomers() {
        List<Customer> all = customerRepository.findAll();

        List<Customer> filtered = all.stream()
            .filter(c -> {
                if (c == null) return false;
                if (c.getStatus() == CustomerStatus.PENDING) return true;
                if (c.getName() == null || c.getName().isBlank()) return true;
                if (c.getSplitter() == null) return true;
                if (c.getAssignedPort() == null) return true;

                // If customer lacks an ONT or lacks a Router then surface in pending list.
                boolean hasOnt = assetRepository.findByAssignedToCustomer_CustomerId(c.getCustomerId())
                        .stream()
                        .anyMatch(a -> a.getAssetType() != null && a.getAssetType().equalsIgnoreCase("ONT"));
                boolean hasRouter = assetRepository.findByAssignedToCustomer_CustomerId(c.getCustomerId())
                        .stream()
                        .anyMatch(a -> a.getAssetType() != null && a.getAssetType().equalsIgnoreCase("ROUTER"));

                if (!hasOnt || !hasRouter) return true;

                return false;
            })
            .collect(Collectors.toList());

        List<Map<String,Object>> out = filtered.stream().map(c -> {
            Map<String,Object> m = new HashMap<>();
            m.put("customerId", c.getCustomerId());
            m.put("name", c.getName());
            m.put("address", c.getAddress());
            m.put("neighborhood", c.getNeighborhood());
            m.put("plan", c.getPlan());
            m.put("connectionType", c.getConnectionType() == null ? null : c.getConnectionType().name());
            m.put("status", c.getStatus() == null ? null : c.getStatus().name());
            m.put("splitterId", c.getSplitter() == null ? null : c.getSplitter().getSplitterId());
            m.put("assignedPort", c.getAssignedPort());
            return m;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(out);
    }
}
