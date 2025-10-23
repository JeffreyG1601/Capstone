package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.WorkOrder;
import java.util.List;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    List<WorkOrder> findByStatus(String status);

    // Use actual PK field name in User entity
    List<WorkOrder> findByAssignedTo_UserId(Long userId);

    // Use actual PK field name in Zone entity
    List<WorkOrder> findByZone_ZoneId(Long zoneId);
}
