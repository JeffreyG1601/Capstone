// src/main/java/com/project1/networkinventory/service/AuditLogService.java
package com.project1.networkinventory.service;

import com.project1.networkinventory.dto.AuditLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AuditLogService {
    AuditLogDTO toDto(com.project1.networkinventory.model.AuditLog e);
    Page<AuditLogDTO> search(String username, String actionType, LocalDateTime from, LocalDateTime to, Pageable p);
    List<AuditLogDTO> findAllForExport(String username, String actionType, LocalDateTime from, LocalDateTime to);
    void save(com.project1.networkinventory.model.AuditLog entry);
}
