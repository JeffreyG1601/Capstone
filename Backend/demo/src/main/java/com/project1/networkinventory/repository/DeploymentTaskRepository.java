package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.DeploymentTask;
import java.util.List;

public interface DeploymentTaskRepository extends JpaRepository<DeploymentTask, Long> {

    // Correct property traversal
    List<DeploymentTask> findByTechnicianTechnicianId(Long technicianId);

    List<DeploymentTask> findByStatus(String status);
}
