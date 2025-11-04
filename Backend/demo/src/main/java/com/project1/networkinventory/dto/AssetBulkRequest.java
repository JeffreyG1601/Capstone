package com.project1.networkinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a single CSV/JSON row for bulk asset upload.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetBulkRequest {
    private String assetType;
    private String model;
    private String serialNumber;
    private String status;
    private String location;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
    
}
