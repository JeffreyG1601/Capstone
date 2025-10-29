// com.project1.networkinventory.dto.AssetDTO.java
package com.project1.networkinventory.dto;

import com.project1.networkinventory.enums.AssetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDTO {
	
    public AssetDTO(Long assetId, String assetType, String model, String serialNumber, AssetStatus status,
			String location, CustomerSummaryDTO assignedToCustomer, LocalDateTime assignedDate) {
		super();
		this.assetId = assetId;
		this.assetType = assetType;
		this.model = model;
		this.serialNumber = serialNumber;
		this.status = status;
		this.location = location;
		this.assignedToCustomer = assignedToCustomer;
		this.assignedDate = assignedDate;
	}
	private Long assetId;
    private String assetType;
    private String model;
    private String serialNumber;
    private AssetStatus status;       // Jackson will use your @JsonCreator in AssetStatus
    private String location;
    private CustomerSummaryDTO assignedToCustomer;
    private LocalDateTime assignedDate;
	public Long getAssetId() {
		return assetId;
	}
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	public String getAssetType() {
		return assetType;
	}
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public AssetStatus getStatus() {
		return status;
	}
	public void setStatus(AssetStatus status) {
		this.status = status;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public CustomerSummaryDTO getAssignedToCustomer() {
		return assignedToCustomer;
	}
	public void setAssignedToCustomer(CustomerSummaryDTO assignedToCustomer) {
		this.assignedToCustomer = assignedToCustomer;
	}
	public LocalDateTime getAssignedDate() {
		return assignedDate;
	}
	public void setAssignedDate(LocalDateTime assignedDate) {
		this.assignedDate = assignedDate;
	}
}
