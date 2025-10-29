package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "task_checklist_item")
public class TaskChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_item_id")
    private Long checklistItemId;

    private String label;

    @Column(columnDefinition = "TEXT")
    private String details;

    private boolean done = false;

	public Long getChecklistItemId() {
		return checklistItemId;
	}

	public void setChecklistItemId(Long checklistItemId) {
		this.checklistItemId = checklistItemId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
}
