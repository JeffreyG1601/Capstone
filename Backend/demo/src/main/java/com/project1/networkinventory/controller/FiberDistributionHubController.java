package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.FiberDistributionHubDTO;
import com.project1.networkinventory.mapper.FiberDistributionHubMapper;
import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.model.Headend;
import com.project1.networkinventory.service.FiberDistributionHubService;
import com.project1.networkinventory.service.SplitterService;
import com.project1.networkinventory.service.HeadendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fdhs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class FiberDistributionHubController {

    private final FiberDistributionHubService fdhService;
    private final SplitterService splitterService;
    private final HeadendService headendService;

    public FiberDistributionHubController(FiberDistributionHubService fdhService, SplitterService splitterService,
			HeadendService headendService) {
		super();
		this.fdhService = fdhService;
		this.splitterService = splitterService;
		this.headendService = headendService;
	}

	@GetMapping
    public ResponseEntity<List<FiberDistributionHubDTO>> list() {
        List<FiberDistributionHub> all = fdhService.getAllFDHs();
        return ResponseEntity.ok(all.stream().map(FiberDistributionHubMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FiberDistributionHubDTO> get(@PathVariable Long id) {
        FiberDistributionHub f = fdhService.getFDHById(id);
        return f == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(FiberDistributionHubMapper.toDto(f));
    }

    @GetMapping("/{id}/splitters")
    public ResponseEntity<?> listSplitters(@PathVariable Long id) {
        // reuse splitterService method
        return ResponseEntity.ok(splitterService.getSplittersByFDHId(id).stream().map(s -> com.project1.networkinventory.mapper.SplitterMapper.toDto(s)).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FiberDistributionHubDTO dto) {
        try {
            // short-circuit: if client provided id and it already exists -> conflict
            if (dto.getId() != null && fdhService.getFDHById(dto.getId()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("FDH with id " + dto.getId() + " already exists");
            }

            FiberDistributionHub entity = FiberDistributionHubMapper.toEntity(dto);
            if (dto.getHeadendId() != null) {
                Headend h = new Headend();
                h.setId(dto.getHeadendId());
                entity.setHeadend(h);
            }
            FiberDistributionHub created = fdhService.createFDH(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(FiberDistributionHubMapper.toDto(created));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Create failed: " + ex.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FiberDistributionHubDTO dto) {
        try {
            FiberDistributionHub entity = FiberDistributionHubMapper.toEntity(dto);
            if (dto.getHeadendId() != null) {
                Headend h = new Headend();
                h.setId(dto.getHeadendId());
                entity.setHeadend(h);
            }
            FiberDistributionHub updated = fdhService.updateFDH(id, entity);
            return ResponseEntity.ok(FiberDistributionHubMapper.toDto(updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fdhService.deleteFDH(id);
        return ResponseEntity.noContent().build();
    }
}
