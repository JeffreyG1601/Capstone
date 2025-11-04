package com.project1.networkinventory.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.project1.networkinventory.enums.Role;

/**
 * Normalizes role strings from DB into enum values.
 * Handles case and common separators (space, -, _).
 */
@Converter(autoApply = false)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String normalized = dbData.trim().toUpperCase().replace('-', '_').replace(' ', '_');
        try {
            return Role.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            // fallback: try simple uppercase
            try { return Role.valueOf(dbData.trim().toUpperCase()); } catch (Exception e) { return null; }
        }
    }
}
