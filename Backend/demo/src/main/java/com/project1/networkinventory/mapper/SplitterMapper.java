package com.project1.networkinventory.mapper;

import com.project1.networkinventory.dto.SplitterDTO;
import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.model.Splitter;

public class SplitterMapper {

    public static SplitterDTO toDto(Splitter s) {
        if (s == null) return null;
        Long fdhId = s.getFiberDistributionHub() != null ? s.getFiberDistributionHub().getId() : null;
        return new SplitterDTO(s.getSplitterId(), fdhId, s.getModel(), s.getPortCapacity(), s.getUsedPorts(), s.getLocation());
    }

    public static Splitter toEntity(SplitterDTO dto) {
        if (dto == null) return null;
        Splitter s = new Splitter();
        s.setSplitterId(dto.getSplitterId());
        s.setModel(dto.getModel());
        s.setPortCapacity(dto.getPortCapacity());
        s.setUsedPorts(dto.getUsedPorts() == null ? 0 : dto.getUsedPorts());
        s.setLocation(dto.getLocation());
        // Don't set fiberDistributionHub here (service will attach if provided)
        return s;
    }
}
