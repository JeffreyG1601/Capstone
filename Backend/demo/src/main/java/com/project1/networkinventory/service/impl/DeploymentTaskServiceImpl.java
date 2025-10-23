package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.DeploymentTask;
import com.project1.networkinventory.repository.DeploymentTaskRepository;
import com.project1.networkinventory.service.DeploymentTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeploymentTaskServiceImpl implements DeploymentTaskService {

    private final DeploymentTaskRepository repository = null;

    @Override
    public DeploymentTask createTask(DeploymentTask task) {
        return repository.save(task);
    }

    @Override
    public List<DeploymentTask> getAllTasks() {
        return repository.findAll();
    }

    @Override
    public DeploymentTask getTaskById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public DeploymentTask updateTask(Long id, DeploymentTask task) {
        task.setTaskId(id);
        return repository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        repository.deleteById(id);
    }
}
