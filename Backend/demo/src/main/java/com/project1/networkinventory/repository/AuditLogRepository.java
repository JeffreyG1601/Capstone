// src/main/java/com/project1/networkinventory/repository/AuditLogRepository.java
package com.project1.networkinventory.repository;

import com.project1.networkinventory.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> { }
