package com.project1.networkinventory.converter;

import com.project1.networkinventory.enums.AssetStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AssetStatusConverter extends CaseInsensitiveEnumConverter<AssetStatus> {
    public AssetStatusConverter() {
        super(AssetStatus.class);
    }
}
