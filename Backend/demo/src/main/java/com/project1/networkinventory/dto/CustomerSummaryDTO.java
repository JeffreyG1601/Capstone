// com.project1.networkinventory.dto.CustomerSummaryDTO.java
package com.project1.networkinventory.dto;

import com.project1.networkinventory.enums.ConnectionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSummaryDTO {
    private Long customerId;
    private String name;
    private String address;
    private String neighborhood;
    private String plan;
    private ConnectionType connectionType;
    private String status; 
    // <-- keep simple string (e.g. "ACTIVE")
	public Long getCustomerId() {
		return customerId;
	}
	public CustomerSummaryDTO(Long customerId, String name, String address, String neighborhood, String plan,
			ConnectionType connectionType2, String status) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.neighborhood = neighborhood;
		this.plan = plan;
		this.connectionType = connectionType2;
		this.status = status;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public ConnectionType getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
