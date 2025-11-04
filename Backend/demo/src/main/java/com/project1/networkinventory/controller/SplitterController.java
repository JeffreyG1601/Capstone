package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.SplitterDTO;
import com.project1.networkinventory.dto.SplitterSummary;
import com.project1.networkinventory.mapper.SplitterMapper;
import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.model.Splitter;
import com.project1.networkinventory.service.SplitterService;
import com.project1.networkinventory.service.FiberDistributionHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/splitters")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SplitterController {

    private final SplitterService splitterService;
    private final FiberDistributionHubService fdhService;
    private final com.project1.networkinventory.repository.SplitterRepository splitterRepository;
    public SplitterController(SplitterService splitterService, FiberDistributionHubService fdhService) {
		super();
		this.splitterService = splitterService;
		this.fdhService = fdhService;
		this.splitterRepository = null;
	}
    @GetMapping("/{fdhId}/splitters")
    public ResponseEntity<List<SplitterSummary>> getSplittersByFdh(@PathVariable Long fdhId) {
        List<Splitter> splitters = splitterRepository.findByFiberDistributionHub_Id(fdhId);
        List<SplitterSummary> result = splitters.stream()
                .map(s -> new SplitterSummary(
                        s.getSplitterId(),
                        s.getModel(),
                        s.getPortCapacity(),
                        s.getUsedPorts()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
	@GetMapping
    public ResponseEntity<List<SplitterDTO>> listAll() {
        List<Splitter> list = splitterService.getAllSplitters();
        return ResponseEntity.ok(list.stream().map(SplitterMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/fdh/{fdhId}")
    public ResponseEntity<List<SplitterDTO>> listByFdh(@PathVariable Long fdhId) {
        List<Splitter> list = splitterService.getSplittersByFDHId(fdhId);
        return ResponseEntity.ok(list.stream().map(SplitterMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SplitterDTO> getById(@PathVariable Long id) {
        Splitter s = splitterService.getSplitterById(id);
        return s == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(SplitterMapper.toDto(s));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SplitterDTO dto) {
        try {
            // If client provided id and it already exists -> conflict
            if (dto.getSplitterId() != null && splitterService.getSplitterById(dto.getSplitterId()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Splitter with id " + dto.getSplitterId() + " already exists");
            }

            Splitter s = SplitterMapper.toEntity(dto);
            if (dto.getFiberDistributionHubId() != null) {
                FiberDistributionHub f = new FiberDistributionHub();
                f.setId(dto.getFiberDistributionHubId());
                s.setFiberDistributionHub(f);
            }
            Splitter created = splitterService.createSplitter(s);
            return ResponseEntity.status(HttpStatus.CREATED).body(SplitterMapper.toDto(created));
        } catch (IllegalArgumentException ex) {
            // validation-like problems (fdh not found or conflict) -> 400 or 409 depending on message
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create failed: " + ex.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SplitterDTO dto) {
        try {
            Splitter s = SplitterMapper.toEntity(dto);
            if (dto.getFiberDistributionHubId() != null) {
                FiberDistributionHub f = new FiberDistributionHub();
                f.setId(dto.getFiberDistributionHubId());
                s.setFiberDistributionHub(f);
            }
            Splitter updated = splitterService.updateSplitter(id, s);
            return ResponseEntity.ok(SplitterMapper.toDto(updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        splitterService.deleteSplitter(id);
        return ResponseEntity.noContent().build();
    }
}
