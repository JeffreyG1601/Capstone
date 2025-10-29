package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.DeploymentTask;
import com.project1.networkinventory.enums.DeploymentStatus;
import java.util.List;

public interface DeploymentTaskRepository extends JpaRepository<DeploymentTask, Long> {

    List<DeploymentTask> findByTechnician_TechnicianId(Long technicianId);

    List<DeploymentTask> findByStatus(DeploymentStatus status);

    // Helpful query: get tasks for a technician with certain statuses
    List<DeploymentTask> findByTechnician_TechnicianIdAndStatusIn(Long technicianId, List<DeploymentStatus> statuses);
}
