package com.project1.networkinventory.converter;

import jakarta.persistence.AttributeConverter;

/**
 * Generic base class to convert string -> enum in case-insensitive manner (spaces -> underscores)
 * Subclass per enum type to enable @Converter(autoApply = true) if desired.
 */
public abstract class CaseInsensitiveEnumConverter<E extends Enum<E>> implements AttributeConverter<E, String> {
    private final Class<E> enumType;

    protected CaseInsensitiveEnumConverter(Class<E> enumType) {
        this.enumType = enumType;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String normalized = dbData.trim().replace(" ", "_").toUpperCase();
        try {
            return Enum.valueOf(enumType, normalized);
        } catch (Exception ex) {
            return null;
        }
    }
}
