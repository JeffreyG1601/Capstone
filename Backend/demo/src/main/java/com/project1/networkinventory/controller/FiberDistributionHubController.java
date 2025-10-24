package com.project1.networkinventory.controller;

import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.model.Splitter;
import com.project1.networkinventory.repository.FiberDistributionHubRepository;
import com.project1.networkinventory.repository.SplitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fdhs")
@RequiredArgsConstructor
public class FiberDistributionHubController {
    private final FiberDistributionHubRepository fdhRepository;
    private final SplitterRepository splitterRepository;

    public FiberDistributionHubController(FiberDistributionHubRepository fdhRepository,
			SplitterRepository splitterRepository) {
		super();
		this.fdhRepository = fdhRepository;
		this.splitterRepository = splitterRepository;
	}

	@GetMapping
    public ResponseEntity<List<FiberDistributionHub>> list() {
        return ResponseEntity.ok(fdhRepository.findAll());
    }

    @GetMapping("/{id}/splitters")
    public ResponseEntity<List<Splitter>> listSplitters(@PathVariable Long id) {
        return ResponseEntity.ok(splitterRepository.findByFiberDistributionHub_Id(id));
    }
}
