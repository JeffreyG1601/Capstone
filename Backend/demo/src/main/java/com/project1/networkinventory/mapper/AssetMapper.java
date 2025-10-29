// com.project1.networkinventory.mapper.AssetMapper.java
package com.project1.networkinventory.mapper;

import com.project1.networkinventory.dto.AssetDTO;
import com.project1.networkinventory.dto.CustomerSummaryDTO;
import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.model.Customer;

import java.util.List;
import java.util.stream.Collectors;

public final class AssetMapper {

    private AssetMapper() {}

    public static AssetDTO toDto(Asset asset) {
        if (asset == null) return null;
        CustomerSummaryDTO cs = null;
        Customer c = asset.getAssignedToCustomer();
        if (c != null) {
            cs = new CustomerSummaryDTO(
                c.getCustomerId(),
                c.getName(),
                c.getAddress(),
                c.getNeighborhood(),
                c.getPlan(),
                c.getConnectionType(),
                c.getStatus() == null ? null : c.getStatus().name() // or keep as enum if you have one
            );
        }
        return new AssetDTO(
            asset.getAssetId(),
            asset.getAssetType(),
            asset.getModel(),
            asset.getSerialNumber(),
            asset.getStatus(),
            asset.getLocation(),
            cs,
            asset.getAssignedDate()
        );
    }

    public static Asset toEntity(AssetDTO dto) {
        if (dto == null) return null;
        Asset a = new Asset();
        a.setAssetId(dto.getAssetId());
        a.setAssetType(dto.getAssetType());
        a.setModel(dto.getModel());
        a.setSerialNumber(dto.getSerialNumber());
        a.setStatus(dto.getStatus()); // AssetStatus handled by enum/json
        a.setLocation(dto.getLocation());
        a.setAssignedDate(dto.getAssignedDate());
        // assignedToCustomer mapping: we only have id in summary maybe — set only id to avoid loading full object
        if (dto.getAssignedToCustomer() != null && dto.getAssignedToCustomer().getCustomerId() != null) {
            Customer c = new Customer();
            c.setCustomerId(dto.getAssignedToCustomer().getCustomerId());
            // don't fill rest; JPA will treat it as reference if needed via merge/attach
            a.setAssignedToCustomer(c);
        } else {
            a.setAssignedToCustomer(null);
        }
        return a;
    }

    public static List<AssetDTO> toDtos(List<Asset> list) {
        return list == null ? List.of() : list.stream().map(AssetMapper::toDto).collect(Collectors.toList());
    }
}
