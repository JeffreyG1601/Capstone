// src/main/java/com/project1/networkinventory/service/impl/RoleServiceImpl.java
package com.project1.networkinventory.service.impl;
import com.project1.networkinventory.dto.RoleDTO;
import com.project1.networkinventory.model.Role;
import com.project1.networkinventory.repository.RoleRepository;
import com.project1.networkinventory.service.RoleService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
  private final RoleRepository repo;
  public RoleServiceImpl(RoleRepository repo){ this.repo = repo; }
  private RoleDTO toDto(Role r){ var d = new RoleDTO(); d.setId(r.getId()); d.setName(r.getName()); d.setDescription(r.getDescription()); return d; }
  private Role toEntity(RoleDTO d){ var r = new Role(); r.setId(d.getId()); r.setName(d.getName()); r.setDescription(d.getDescription()); return r; }
  @Override public RoleDTO save(RoleDTO dto){ return toDto(repo.save(toEntity(dto))); }
  @Override public List<RoleDTO> listAll(){ return repo.findAll().stream().map(this::toDto).collect(Collectors.toList()); }
  @Override public void delete(Long id){ repo.deleteById(id); }
}
