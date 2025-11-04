package com.project1.networkinventory.dto;

public class SplitterSummary {

    private Long splitterId;
    private String model;
    private Integer portCapacity;
    private Integer usedPorts;
    private Integer availablePorts;

    public SplitterSummary() {}

    public SplitterSummary(Long splitterId, String model, Integer portCapacity, Integer usedPorts) {
        this.splitterId = splitterId;
        this.model = model;
        this.portCapacity = portCapacity;
        this.usedPorts = usedPorts == null ? 0 : usedPorts;
        this.availablePorts = portCapacity == null ? 0 : portCapacity - this.usedPorts;
    }

    public Long getSplitterId() { return splitterId; }
    public void setSplitterId(Long splitterId) { this.splitterId = splitterId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getPortCapacity() { return portCapacity; }
    public void setPortCapacity(Integer portCapacity) { this.portCapacity = portCapacity; }

    public Integer getUsedPorts() { return usedPorts; }
    public void setUsedPorts(Integer usedPorts) { this.usedPorts = usedPorts; }

    public Integer getAvailablePorts() { return availablePorts; }
    public void setAvailablePorts(Integer availablePorts) { this.availablePorts = availablePorts; }
}
