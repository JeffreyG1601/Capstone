package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.project1.networkinventory.enums.AssetStatus;

@Data
@Entity
@Table(name = "asset")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetId;  // Matches BIGINT in SQL

    private String assetType; // Should match ENUM values in SQL

    private String model;
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private AssetStatus status; // Matches SQL ENUM

    private String location;

    @ManyToOne
    @JoinColumn(name = "assigned_to_customer_id")
    private Customer assignedToCustomer; // Matches BIGINT FK in SQL

    private LocalDateTime assignedDate;

    // Full getters and setters
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public String getAssetType() { return assetType; }
    public void setAssetType(String assetType) { this.assetType = assetType; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public AssetStatus getStatus() { return status; }
    public void setStatus(AssetStatus status) { this.status = status; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Customer getAssignedToCustomer() { return assignedToCustomer; }
    public void setAssignedToCustomer(Customer assignedToCustomer) { this.assignedToCustomer = assignedToCustomer; }
    public LocalDateTime getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDateTime assignedDate) { this.assignedDate = assignedDate; }
}
