package com.project1.networkinventory.controller;

import org.springframework.web.bind.annotation.*;
import com.project1.networkinventory.model.DeploymentTask;
import com.project1.networkinventory.service.TechnicianTaskService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/technician/tasks")
public class TechnicianTaskController {

    private final TechnicianTaskService taskService;

    public TechnicianTaskController(TechnicianTaskService taskService) {
        this.taskService = taskService;
    }

    // Get tasks assigned to a technician
    @GetMapping("/technician/{techId}")
    public List<DeploymentTask> getTasks(@PathVariable Long techId) {
        return taskService.getTasksForTechnician(techId);
    }

    // Start a task
    @PostMapping("/{taskId}/start")
    public DeploymentTask start(@PathVariable Long taskId) {
        return taskService.startTask(taskId);
    }

    // Toggle checklist item done/undone
    @PostMapping("/{taskId}/checklist/{itemId}")
    public DeploymentTask toggleChecklist(@PathVariable Long taskId,
                                          @PathVariable Long itemId,
                                          @RequestParam(name = "done", defaultValue = "true") boolean done) {
        return taskService.toggleChecklistItem(taskId, itemId, done);
    }

    // Add an installation note
    @PostMapping("/{taskId}/notes")
    public DeploymentTask addNote(@PathVariable Long taskId, @RequestBody Map<String, String> payload) {
        String author = payload.getOrDefault("author", "Technician");
        String note = payload.getOrDefault("note", "");
        return taskService.addNote(taskId, author, note);
    }

    // Complete the task (and trigger updates)
    @PostMapping("/{taskId}/complete")
    public Map<String, Object> complete(@PathVariable Long taskId) {
        DeploymentTask t = taskService.completeTask(taskId);
        Map<String, Object> response = new HashMap<>();
        response.put("taskId", t.getTaskId());
        response.put("status", t.getStatus());
        response.put("message", "Task marked completed. Customer and inventory updates attempted.");
        return response;
    }
}
