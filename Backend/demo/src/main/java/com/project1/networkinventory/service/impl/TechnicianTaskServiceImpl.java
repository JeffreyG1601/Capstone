package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.*;
import com.project1.networkinventory.repository.*;
import com.project1.networkinventory.enums.DeploymentStatus;
import com.project1.networkinventory.enums.CustomerStatus;
import com.project1.networkinventory.enums.AssetStatus;
import com.project1.networkinventory.service.TechnicianTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TechnicianTaskServiceImpl implements TechnicianTaskService {

    private final DeploymentTaskRepository taskRepo;
    private final CustomerRepository customerRepo;
    private final AssetRepository assetRepo;

    public TechnicianTaskServiceImpl(DeploymentTaskRepository taskRepo,
                                     CustomerRepository customerRepo,
                                     AssetRepository assetRepo) {
        this.taskRepo = taskRepo;
        this.customerRepo = customerRepo;
        this.assetRepo = assetRepo;
    }

    @Override
    public List<DeploymentTask> getTasksForTechnician(Long techId) {
        return taskRepo.findByTechnician_TechnicianId(techId);
    }

    @Override
    @Transactional
    public DeploymentTask startTask(Long taskId) {
        DeploymentTask t = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        t.setStatus(DeploymentStatus.IN_PROGRESS);
        t.setUpdatedAt(LocalDateTime.now());
        return taskRepo.save(t);
    }

    @Override
    @Transactional
    public DeploymentTask toggleChecklistItem(Long taskId, Long itemId, boolean done) {
        DeploymentTask t = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        t.getChecklist().stream()
                .filter(i -> i.getChecklistItemId().equals(itemId))
                .findFirst()
                .ifPresent(i -> i.setDone(done));
        t.setUpdatedAt(LocalDateTime.now());
        return taskRepo.save(t);
    }

    @Override
    @Transactional
    public DeploymentTask addNote(Long taskId, String author, String noteText) {
        DeploymentTask t = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        InstallationNote note = new InstallationNote();
        note.setAuthor(author != null ? author : "Technician");
        note.setNote(noteText);
        note.setTimestamp(LocalDateTime.now());
        t.addNote(note);
        t.setUpdatedAt(LocalDateTime.now());
        return taskRepo.save(t);
    }

    @Override
    @Transactional
    public DeploymentTask completeTask(Long taskId) {
        DeploymentTask t = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        // simple validation: ensure checklist items are all done (optional)
        boolean allDone = t.getChecklist().stream().allMatch(TaskChecklistItem::isDone);
        // You may choose to enforce this:
        // if (!allDone) throw new RuntimeException("Not all checklist items are completed.");

        t.setStatus(DeploymentStatus.COMPLETED);
        t.setUpdatedAt(LocalDateTime.now());
        taskRepo.save(t);

        // Update customer status to ACTIVE if present
        if (t.getCustomer() != null) {
            Long custId = t.getCustomer().getCustomerId();
            Optional<Customer> custOpt = customerRepo.findById(custId);
            custOpt.ifPresent(c -> {
                c.setStatus(CustomerStatus.ACTIVE);
                customerRepo.save(c);
            });
        }

        // Asset assignment/marking logic can be added here if checklist contains serials

        return t;
    }
}
