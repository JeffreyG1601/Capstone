package com.project1.networkinventory.service;

import com.project1.networkinventory.model.FiberDistributionHub;
import java.util.List;

public interface FiberDistributionHubService {
    FiberDistributionHub createFDH(FiberDistributionHub fdh);
    List<FiberDistributionHub> getAllFDHs();
    FiberDistributionHub getFDHById(Long id);
    FiberDistributionHub updateFDH(Long id, FiberDistributionHub fdh);
    void deleteFDH(Long id);
}
