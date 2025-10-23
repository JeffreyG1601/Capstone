package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "network_link")
public class NetworkLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sourceDeviceId;
    private Long targetDeviceId;
    private String connectionType; // Fiber, Ethernet
    private Double distanceKm;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSourceDeviceId() { return sourceDeviceId; }
    public void setSourceDeviceId(Long sourceDeviceId) { this.sourceDeviceId = sourceDeviceId; }

    public Long getTargetDeviceId() { return targetDeviceId; }
    public void setTargetDeviceId(Long targetDeviceId) { this.targetDeviceId = targetDeviceId; }

    public String getConnectionType() { return connectionType; }
    public void setConnectionType(String connectionType) { this.connectionType = connectionType; }

    public Double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }
}
