package com.project1.networkinventory.service;

import com.project1.networkinventory.model.Technician;
import java.util.List;

public interface TechnicianService {
    Technician createTechnician(Technician tech);
    List<Technician> getAllTechnicians();
    Technician getTechnicianById(Long id);
    Technician updateTechnician(Long id, Technician tech);
    void deleteTechnician(Long id);
}
