package com.project1.networkinventory.mapper;

import com.project1.networkinventory.dto.FiberDistributionHubDTO;
import com.project1.networkinventory.dto.HeadendDTO;
import com.project1.networkinventory.model.Headend;

import java.util.List;
import java.util.stream.Collectors;

public class HeadendMapper {

    public static HeadendDTO toDto(Headend h) {
        if (h == null) return null;
        List<FiberDistributionHubDTO> fdhs = null;
        if (h.getFdhList() != null) {
            fdhs = h.getFdhList().stream().map(FiberDistributionHubMapper::toDto).collect(Collectors.toList());
        }
        return new HeadendDTO(h.getId(), h.getName(), h.getLocation(), h.getRegion(), fdhs);
    }

    public static Headend toEntity(HeadendDTO dto) {
        if (dto == null) return null;
        Headend h = new Headend();
        h.setId(dto.getId());
        h.setName(dto.getName());
        h.setLocation(dto.getLocation());
        h.setRegion(dto.getRegion());
        // fdhList linking handled in service
        return h;
    }
}
