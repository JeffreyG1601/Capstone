package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.HeadendDTO;
import com.project1.networkinventory.mapper.HeadendMapper;
import com.project1.networkinventory.model.Headend;
import com.project1.networkinventory.service.HeadendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/headends")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class HeadendController {

    private final HeadendService headendService;

    public HeadendController(HeadendService headendService) {
		super();
		this.headendService = headendService;
	}

	@GetMapping
    public ResponseEntity<List<HeadendDTO>> listAll() {
        List<Headend> all = headendService.getAllHeadends();
        return ResponseEntity.ok(all.stream().map(HeadendMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeadendDTO> getById(@PathVariable Long id) {
        Headend h = headendService.getHeadendById(id);
        return h == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(HeadendMapper.toDto(h));
    }

    @PostMapping
    public ResponseEntity<HeadendDTO> create(@RequestBody HeadendDTO dto) {
        if (dto.getId() != null && headendService.getHeadendById(dto.getId()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Headend entity = HeadendMapper.toEntity(dto);
        try {
            Headend created = headendService.createHeadend(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(HeadendMapper.toDto(created));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeadendDTO> update(@PathVariable Long id, @RequestBody HeadendDTO dto) {
        Headend entity = HeadendMapper.toEntity(dto);
        Headend updated = headendService.updateHeadend(id, entity);
        return ResponseEntity.ok(HeadendMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        headendService.deleteHeadend(id);
        return ResponseEntity.noContent().build();
    }
}
