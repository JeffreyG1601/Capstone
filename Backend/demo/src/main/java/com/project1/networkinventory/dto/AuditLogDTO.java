// src/main/java/com/project1/networkinventory/dto/AuditLogDTO.java
package com.project1.networkinventory.dto;

import java.time.LocalDateTime;
public class AuditLogDTO {
    private Long id;
    private String username;
    private String actionType;
    private String description;
    private String ipAddress;
    private LocalDateTime timestamp;
    // getters & setters
    
	public Long getId() {
		return id;
	}
	public AuditLogDTO(Long id, String username, String actionType, String description, String ipAddress,
			LocalDateTime timestamp) {
		super();
		this.id = id;
		this.username = username;
		this.actionType = actionType;
		this.description = description;
		this.ipAddress = ipAddress;
		this.timestamp = timestamp;
	}
	public AuditLogDTO() {
		super();
		this.id = id;
		this.username = username;
		this.actionType = actionType;
		this.description = description;
		this.ipAddress = ipAddress;
		this.timestamp = timestamp;
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
    
}
