package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.model.Technician;
import com.project1.networkinventory.repository.TechnicianRepository;
import com.project1.networkinventory.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository repository = null;

    @Override
    public Technician createTechnician(Technician tech) {
        return repository.save(tech);
    }

    @Override
    public List<Technician> getAllTechnicians() {
        return repository.findAll();
    }

    @Override
    public Technician getTechnicianById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Technician updateTechnician(Long id, Technician tech) {
        tech.setTechnicianId(id);
        return repository.save(tech);
    }

    @Override
    public void deleteTechnician(Long id) {
        repository.deleteById(id);
    }
}
