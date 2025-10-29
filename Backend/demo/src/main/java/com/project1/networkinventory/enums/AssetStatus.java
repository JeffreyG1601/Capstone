package com.project1.networkinventory.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AssetStatus {
    AVAILABLE,
    ASSIGNED,
    IN_USE,
    FAULTY,
    RETIRED;

    // Serialize as UPPERCASE name()
    @JsonValue
    public String toValue() {
        return name();
    }

    // Accept case-insensitive incoming JSON values
    @JsonCreator
    public static AssetStatus fromJson(String value) {
        return fromString(value);
    }

    // Tolerant parser for DB / JSON values: case-insensitive, spaces -> underscores
    public static AssetStatus fromString(String value) {
        if (value == null) return null;
        String normalized = value.trim().replace(" ", "_").toUpperCase();
        try {
            return AssetStatus.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
