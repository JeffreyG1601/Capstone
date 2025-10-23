package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "assigned_assets")
public class AssignedAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // matches BIGINT

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; // FK to Customer

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset; // FK to Asset

    private LocalDateTime assignedOn = LocalDateTime.now();

    // Full getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }

    public LocalDateTime getAssignedOn() { return assignedOn; }
    public void setAssignedOn(LocalDateTime assignedOn) { this.assignedOn = assignedOn; }
}
