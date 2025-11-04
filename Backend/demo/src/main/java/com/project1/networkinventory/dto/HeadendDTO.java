package com.project1.networkinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeadendDTO {
    private Long id;
    private String name;
    private String location;
    private String region;
    // optional list of FDH DTOs (may be omitted or empty)
    private List<FiberDistributionHubDTO> fdhList;
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
	public List<FiberDistributionHubDTO> getFdhList() {
		return fdhList;
	}
	public void setFdhList(List<FiberDistributionHubDTO> fdhList) {
		this.fdhList = fdhList;
	}
	public HeadendDTO(Long id, String name, String location, String region, List<FiberDistributionHubDTO> fdhList) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.region = region;
		this.fdhList = fdhList;
	}
    
}
