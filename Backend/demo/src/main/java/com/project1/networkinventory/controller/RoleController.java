// src/main/java/com/project1/networkinventory/controller/RoleController.java
package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.RoleDTO;
import com.project1.networkinventory.enums.Role;
import com.project1.networkinventory.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/roles")
public class RoleController {
  private final RoleService svc;
  public RoleController(RoleService svc){ this.svc = svc; }

  @GetMapping
  public List<RoleDTO> list(){ return svc.listAll(); }

  @PostMapping
  public RoleDTO create(@RequestBody RoleDTO dto){ return svc.save(dto); }

  @PutMapping
  public RoleDTO update(@RequestBody RoleDTO dto){ return svc.save(dto); }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id){ svc.delete(id); }

  // new: return enum options for dropdowns
  @GetMapping("/options")
  public List<String> options() {
    return Arrays.stream(Role.values()).map(Enum::name).collect(Collectors.toList());
  }
}
