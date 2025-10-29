package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.project1.networkinventory.enums.WorkOrderStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status;

    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private String notes;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public User getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}
	public Zone getZone() {
		return zone;
	}
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	public WorkOrderStatus getStatus() {
		return status;
	}
	public void setStatus(WorkOrderStatus status) {
		this.status = status;
	}
	public LocalDateTime getScheduledDate() {
		return scheduledDate;
	}
	public void setScheduledDate(LocalDateTime scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	public LocalDateTime getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(LocalDateTime completedDate) {
		this.completedDate = completedDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
    
    // Getters and Setters (optional if using Lombok)
}
