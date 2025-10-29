package com.project1.networkinventory.service;

import com.project1.networkinventory.model.DeploymentTask;

import java.util.List;

public interface TechnicianTaskService {
    List<DeploymentTask> getTasksForTechnician(Long techId);
    DeploymentTask startTask(Long taskId);
    DeploymentTask toggleChecklistItem(Long taskId, Long itemId, boolean done);
    DeploymentTask addNote(Long taskId, String author, String note);
    DeploymentTask completeTask(Long taskId);
}
