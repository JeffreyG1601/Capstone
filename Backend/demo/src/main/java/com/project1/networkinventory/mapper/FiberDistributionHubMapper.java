package com.project1.networkinventory.mapper;

import com.project1.networkinventory.dto.FiberDistributionHubDTO;
import com.project1.networkinventory.dto.SplitterDTO;
import com.project1.networkinventory.model.FiberDistributionHub;
import com.project1.networkinventory.model.Splitter;

import java.util.List;
import java.util.stream.Collectors;

public class FiberDistributionHubMapper {

    public static FiberDistributionHubDTO toDto(FiberDistributionHub fdh) {
        if (fdh == null) return null;
        List<SplitterDTO> splitters = null;
        if (fdh.getSplitters() != null) {
            splitters = fdh.getSplitters().stream().map(SplitterMapper::toDto).collect(Collectors.toList());
        }
        Long headendId = fdh.getHeadend() != null ? fdh.getHeadend().getId() : null;
        return new FiberDistributionHubDTO(fdh.getId(), fdh.getName(), fdh.getLocation(), fdh.getRegion(), fdh.getMaxPorts(), splitters, headendId);
    }

    public static FiberDistributionHub toEntity(FiberDistributionHubDTO dto) {
        if (dto == null) return null;
        FiberDistributionHub f = new FiberDistributionHub();
        f.setId(dto.getId());
        f.setName(dto.getName());
        f.setLocation(dto.getLocation());
        f.setRegion(dto.getRegion());
        f.setMaxPorts(dto.getMaxPorts() == null ? 0 : dto.getMaxPorts());
        // splitters & headend linking should be handled in service layer
        return f;
    }
}
