package com.project1.networkinventory.mapper;

import com.project1.networkinventory.dto.AssetBulkRequest;
import com.project1.networkinventory.enums.AssetStatus;
import com.project1.networkinventory.model.Asset;

public class AssetBulkMapper {

    public static Asset toEntity(AssetBulkRequest req) {
        if (req == null) return null;
        Asset a = new Asset();
        a.setAssetType(req.getAssetType());
        a.setModel(req.getModel());
        a.setSerialNumber(req.getSerialNumber());
        // parse tolerant status
        AssetStatus s = AssetStatus.fromString(req.getStatus());
        a.setStatus(s);
        a.setLocation(req.getLocation());
        // assignedToCustomer left null for bulk uploads; assignedDate null
        return a;
    }
}
