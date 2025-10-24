package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.OnboardRequest;
import com.project1.networkinventory.dto.OnboardResponse;
import com.project1.networkinventory.service.PlannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planner")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PlannerController {

    private final PlannerService plannerService;

    public PlannerController(PlannerService plannerService) {
		super();
		this.plannerService = plannerService;
	}

	@PostMapping("/onboard")
    public ResponseEntity<?> onboardCustomer(@Valid @RequestBody OnboardRequest req) {
        try {
            OnboardResponse resp = plannerService.onboardCustomer(req);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to onboard: " + e.getMessage());
        }
    }

    // Utility endpoints for planner UI

    @GetMapping("/fdhs")
    public ResponseEntity<?> listFDHs(@RequestParam(required = false) Long id, final com.project1.networkinventory.repository.FiberDistributionHubRepository fdhRepository) {
        // Note: simple quick helper - if you prefer, create a proper FDHService
        if (id != null) {
            return fdhRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.ok(fdhRepository.findAll());
        }
    }
}
