package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "splitter")
public class Splitter {

    @Id
    @Column(name = "splitter_id")
    private Long splitterId; // CHANGED to Long ✅

    @ManyToOne
    @JoinColumn(name = "fdh_id")
    private FiberDistributionHub fiberDistributionHub;

    private String model;
    private Integer portCapacity;
    private Integer usedPorts;
    private String location;

    // Getters and Setters
    public Long getSplitterId() { return splitterId; }
    public void setSplitterId(Long splitterId) { this.splitterId = splitterId; }

    public FiberDistributionHub getFiberDistributionHub() { return fiberDistributionHub; }
    public void setFiberDistributionHub(FiberDistributionHub fiberDistributionHub) { this.fiberDistributionHub = fiberDistributionHub; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getPortCapacity() { return portCapacity; }
    public void setPortCapacity(Integer portCapacity) { this.portCapacity = portCapacity; }

    public Integer getUsedPorts() { return usedPorts; }
    public void setUsedPorts(Integer usedPorts) { this.usedPorts = usedPorts; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
