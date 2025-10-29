package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.project1.networkinventory.enums.DeploymentStatus;

@Data
@Entity
@Table(name = "deployment_task")
public class DeploymentTask {

    public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Technician getTechnician() {
		return technician;
	}

	public void setTechnician(Technician technician) {
		this.technician = technician;
	}

	public DeploymentStatus getStatus() {
		return status;
	}

	public void setStatus(DeploymentStatus status) {
		this.status = status;
	}

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<TaskChecklistItem> getChecklist() {
		return checklist;
	}

	public void setChecklist(List<TaskChecklistItem> checklist) {
		this.checklist = checklist;
	}

	public List<InstallationNote> getNotesList() {
		return notesList;
	}

	public void setNotesList(List<InstallationNote> notesList) {
		this.notesList = notesList;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id", nullable = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Technician technician;

    @Enumerated(EnumType.STRING)
    private DeploymentStatus status;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Checklist items for the task (e.g., "Verify ONT power", "Test PPPoE")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "task_id") // foreign key in child table
    private List<TaskChecklistItem> checklist = new ArrayList<>();

    // Installation notes / comments added during the task
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "task_id")
    private List<InstallationNote> notesList = new ArrayList<>();

    // Convenience helpers
    public void addChecklistItem(TaskChecklistItem item) {
        checklist.add(item);
    }

    public void addNote(InstallationNote note) {
        notesList.add(note);
    }
}
