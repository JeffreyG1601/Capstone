package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.AuditLog;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Traverse the user object properly
    List<AuditLog> findByUserUserId(Long userId);

    // Match the actual field name in AuditLog
    List<AuditLog> findByActionTypeContainingIgnoreCase(String actionType);
}
