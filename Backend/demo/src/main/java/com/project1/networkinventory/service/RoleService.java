// src/main/java/com/project1/networkinventory/service/RoleService.java
package com.project1.networkinventory.service;
import com.project1.networkinventory.dto.RoleDTO;
import java.util.List;
public interface RoleService {
  RoleDTO save(RoleDTO dto);
  List<RoleDTO> listAll();
  void delete(Long id);
}
