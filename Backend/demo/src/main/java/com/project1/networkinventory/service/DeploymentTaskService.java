package com.project1.networkinventory.service;

import com.project1.networkinventory.model.DeploymentTask;
import java.util.List;

public interface DeploymentTaskService {
    DeploymentTask createTask(DeploymentTask task);
    List<DeploymentTask> getAllTasks();
    DeploymentTask getTaskById(Long id);
    DeploymentTask updateTask(Long id, DeploymentTask task);
    void deleteTask(Long id);
}
