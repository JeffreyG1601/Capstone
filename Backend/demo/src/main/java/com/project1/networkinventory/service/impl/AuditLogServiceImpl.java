// src/main/java/com/project1/networkinventory/service/impl/AuditLogServiceImpl.java
package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.dto.AuditLogDTO;
import com.project1.networkinventory.model.AuditLog;
import com.project1.networkinventory.repository.AuditLogRepository;
import com.project1.networkinventory.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repo;
    public AuditLogServiceImpl(AuditLogRepository repo) { this.repo = repo; }

    @Override
    public AuditLogDTO toDto(AuditLog e){
        AuditLogDTO d = new AuditLogDTO();
        d.setId(e.getId()); d.setUsername(e.getUsername()); d.setActionType(e.getActionType());
        d.setDescription(e.getDescription()); d.setIpAddress(e.getIpAddress()); d.setTimestamp(e.getTimestamp());
        return d;
    }

    @Override
    public Page<AuditLogDTO> search(String username, String actionType, LocalDateTime from, LocalDateTime to, Pageable p) {
        Specification<AuditLog> spec = (root, q, cb) -> {
            var preds = cb.conjunction();
            if (username != null) preds = cb.and(preds, cb.equal(root.get("username"), username));
            if (actionType != null) preds = cb.and(preds, cb.equal(root.get("actionType"), actionType));
            if (from != null) preds = cb.and(preds, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
            if (to != null) preds = cb.and(preds, cb.lessThanOrEqualTo(root.get("timestamp"), to));
            return preds;
        };
        return repo.findAll(spec, p).map(this::toDto);
    }

    @Override
    public List<AuditLogDTO> findAllForExport(String username, String actionType, LocalDateTime from, LocalDateTime to) {
        var page = search(username, actionType, from, to, Pageable.unpaged());
        return page.getContent();
    }

    @Override
    public void save(AuditLog entry) { repo.save(entry); }
}
