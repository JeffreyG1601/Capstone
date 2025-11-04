package com.project1.networkinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SplitterDTO {
    private Long splitterId;
    // optional link by id
    private Long fiberDistributionHubId;
    private String model;
    private Integer portCapacity;
    private Integer usedPorts;
    private String location;
	public Long getSplitterId() {
		return splitterId;
	}
	public void setSplitterId(Long splitterId) {
		this.splitterId = splitterId;
	}
	public Long getFiberDistributionHubId() {
		return fiberDistributionHubId;
	}
	public void setFiberDistributionHubId(Long fiberDistributionHubId) {
		this.fiberDistributionHubId = fiberDistributionHubId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getPortCapacity() {
		return portCapacity;
	}
	public void setPortCapacity(Integer portCapacity) {
		this.portCapacity = portCapacity;
	}
	public Integer getUsedPorts() {
		return usedPorts;
	}
	public void setUsedPorts(Integer usedPorts) {
		this.usedPorts = usedPorts;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public SplitterDTO(Long splitterId, Long fiberDistributionHubId, String model, Integer portCapacity,
			Integer usedPorts, String location) {
		super();
		this.splitterId = splitterId;
		this.fiberDistributionHubId = fiberDistributionHubId;
		this.model = model;
		this.portCapacity = portCapacity;
		this.usedPorts = usedPorts;
		this.location = location;
	}
    
}
