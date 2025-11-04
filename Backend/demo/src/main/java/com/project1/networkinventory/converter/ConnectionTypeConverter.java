package com.project1.networkinventory.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.project1.networkinventory.enums.ConnectionType;

@Converter(autoApply = true)
public class ConnectionTypeConverter implements AttributeConverter<ConnectionType, String> {

    @Override
    public String convertToDatabaseColumn(ConnectionType attribute) {
        return attribute == null ? null : attribute.name(); // store UPPERCASE
    }

    @Override
    public ConnectionType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return ConnectionType.valueOf(dbData.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null; // or throw a clearer exception if you prefer
        }
    }
}
