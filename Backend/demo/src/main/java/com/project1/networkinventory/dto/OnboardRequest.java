package com.project1.networkinventory.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.project1.networkinventory.enums.ConnectionType;
import java.time.LocalDateTime;

public class OnboardRequest {

    // Customer details
    @JsonAlias({"customerName", "name"})
    private String name;
    private String address;
    private String neighborhood;
    private String plan;
    private ConnectionType connectionType;

    // Network selection
    private Long fdhId;

    @JsonAlias({"splitterId", "splitter_id", "splitter"})
    private Long splitterId;

    private Integer assignedPort;

    // Asset assignment
    private Long ontAssetId;
    private Long routerAssetId;

    // Task assignment
    private Long technicianId;
    private LocalDateTime scheduledDate;

    // Getters / Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }

    public ConnectionType getConnectionType() { return connectionType; }
    public void setConnectionType(ConnectionType connectionType) { this.connectionType = connectionType; }

    public Long getFdhId() { return fdhId; }
    public void setFdhId(Long fdhId) { this.fdhId = fdhId; }

    public Long getSplitterId() { return splitterId; }
    public void setSplitterId(Long splitterId) { this.splitterId = splitterId; }

    public Integer getAssignedPort() { return assignedPort; }
    public void setAssignedPort(Integer assignedPort) { this.assignedPort = assignedPort; }

    public Long getOntAssetId() { return ontAssetId; }
    public void setOntAssetId(Long ontAssetId) { this.ontAssetId = ontAssetId; }

    public Long getRouterAssetId() { return routerAssetId; }
    public void setRouterAssetId(Long routerAssetId) { this.routerAssetId = routerAssetId; }

    public Long getTechnicianId() { return technicianId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }

    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }

    @Override
    public String toString() {
        return "OnboardRequest{" +
                "name='" + name + '\'' +
                ", fdhId=" + fdhId +
                ", splitterId=" + splitterId +
                ", assignedPort=" + assignedPort +
                ", ontAssetId=" + ontAssetId +
                ", routerAssetId=" + routerAssetId +
                ", technicianId=" + technicianId +
                '}';
    }
}
