// src/main/java/com/project1/networkinventory/model/AuditLog.java
package com.project1.networkinventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;
    private String username;
    private String actionType;
    @Column(length = 2000)
    private String description;
    private String ipAddress;
    private LocalDateTime timestamp;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

    // getters & setters
    
}
