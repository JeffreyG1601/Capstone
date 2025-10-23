package com.project1.networkinventory.controller;

import com.project1.networkinventory.model.DeploymentTask;
import com.project1.networkinventory.service.DeploymentTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class DeploymentTaskController {

    private final DeploymentTaskService deploymentTaskService = null;

    @PostMapping
    public DeploymentTask createTask(@RequestBody DeploymentTask task) {
        return deploymentTaskService.createTask(task);
    }

    @GetMapping
    public List<DeploymentTask> getAllTasks() {
        return deploymentTaskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public DeploymentTask getTaskById(@PathVariable Long id) {
        return deploymentTaskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public DeploymentTask updateTask(@PathVariable Long id, @RequestBody DeploymentTask task) {
        return deploymentTaskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        deploymentTaskService.deleteTask(id);
    }
}
