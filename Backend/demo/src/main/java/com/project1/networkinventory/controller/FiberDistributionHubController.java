package com.project1.networkinventory.controller;

import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.service.FiberDistributionHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fdhs")
@RequiredArgsConstructor
public class FiberDistributionHubController {

    private final FiberDistributionHubService fdhService = null;

    @PostMapping
    public FiberDistributionHub createFDH(@RequestBody FiberDistributionHub fdh) {
        return fdhService.createFDH(fdh);
    }

    @GetMapping
    public List<FiberDistributionHub> getAllFDHs() {
        return fdhService.getAllFDHs();
    }

    @GetMapping("/{id}")
    public FiberDistributionHub getFDHById(@PathVariable Long id) {
        return fdhService.getFDHById(id);
    }

    @PutMapping("/{id}")
    public FiberDistributionHub updateFDH(@PathVariable Long id, @RequestBody FiberDistributionHub fdh) {
        return fdhService.updateFDH(id, fdh);
    }

    @DeleteMapping("/{id}")
    public void deleteFDH(@PathVariable Long id) {
        fdhService.deleteFDH(id);
    }
}
