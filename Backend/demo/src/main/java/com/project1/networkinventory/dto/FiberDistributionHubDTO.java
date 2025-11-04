package com.project1.networkinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiberDistributionHubDTO {
    private Long id;
    private String name;
    private String location;
    private String region;
    private Integer maxPorts;
    // optional nested splitter DTOs (may be null/empty for list endpoints)
    private List<SplitterDTO> splitters;
    // optional headend id reference
    private Long headendId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Integer getMaxPorts() {
		return maxPorts;
	}
	public void setMaxPorts(Integer maxPorts) {
		this.maxPorts = maxPorts;
	}
	public List<SplitterDTO> getSplitters() {
		return splitters;
	}
	public void setSplitters(List<SplitterDTO> splitters) {
		this.splitters = splitters;
	}
	public Long getHeadendId() {
		return headendId;
	}
	public void setHeadendId(Long headendId) {
		this.headendId = headendId;
	}
	public FiberDistributionHubDTO(Long id, String name, String location, String region, Integer maxPorts,
			List<SplitterDTO> splitters, Long headendId) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.region = region;
		this.maxPorts = maxPorts;
		this.splitters = splitters;
		this.headendId = headendId;
	}
    
}
