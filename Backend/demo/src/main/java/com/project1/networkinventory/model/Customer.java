package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import com.project1.networkinventory.enums.ConnectionType;
import com.project1.networkinventory.enums.CustomerStatus;

@Data
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String neighborhood;
    private String plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "connection_type")
    private ConnectionType connectionType;

    private CustomerStatus status;

    @ManyToOne
    @JoinColumn(name = "splitter_id")
    private Splitter splitter;

    @Column(name = "assigned_port")
    private Integer assignedPort;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

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

    public CustomerStatus getStatus() { return status; }
    public void setStatus(CustomerStatus status) { this.status = status; }

    public Splitter getSplitter() { return splitter; }
    public void setSplitter(Splitter splitter) { this.splitter = splitter; }

    public Integer getAssignedPort() { return assignedPort; }
    public void setAssignedPort(Integer assignedPort) { this.assignedPort = assignedPort; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
