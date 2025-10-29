package com.project1.networkinventory.dto;

import java.time.LocalDate;

import com.project1.networkinventory.enums.ConnectionType;

public class OnboardRequest {
    // Customer
    public String name;
    public String address;
    public String neighborhood;
    public String plan;
    public ConnectionType connectionType; // "Wired" or "Wireless"
    // Network selection
    public Long fdhId;       // optional if auto-mapped, recommended
    public Long splitterId;
    public Integer assignedPort;
    // Assets (IDs of assets to assign)
    public Long ontAssetId;
    public Long routerAssetId;
    // Task assignment
    public Long technicianId; // optional — planner can pick a tech
    public LocalDate scheduledDate;
    // optional
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public ConnectionType getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}
	public Long getFdhId() {
		return fdhId;
	}
	public void setFdhId(Long fdhId) {
		this.fdhId = fdhId;
	}
	public Long getSplitterId() {
		return splitterId;
	}
	public void setSplitterId(Long splitterId) {
		this.splitterId = splitterId;
	}
	public Integer getAssignedPort() {
		return assignedPort;
	}
	public void setAssignedPort(Integer assignedPort) {
		this.assignedPort = assignedPort;
	}
	public Long getOntAssetId() {
		return ontAssetId;
	}
	public void setOntAssetId(Long ontAssetId) {
		this.ontAssetId = ontAssetId;
	}
	public Long getRouterAssetId() {
		return routerAssetId;
	}
	public void setRouterAssetId(Long routerAssetId) {
		this.routerAssetId = routerAssetId;
	}
	public Long getTechnicianId() {
		return technicianId;
	}
	public void setTechnicianId(Long technicianId) {
		this.technicianId = technicianId;
	}
	public LocalDate getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
}
