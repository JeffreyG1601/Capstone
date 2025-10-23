package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.MaintenanceLog;
import java.util.List;

public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {

    // Corrected to match Asset.assetId field
    List<MaintenanceLog> findByAssetAssetId(Long assetId);

    // Already correct
    List<MaintenanceLog> findByTechnicianUserId(Long userId);
}
