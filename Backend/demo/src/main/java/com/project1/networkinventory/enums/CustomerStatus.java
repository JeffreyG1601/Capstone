package com.project1.networkinventory.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerStatus {
    ACTIVE,
    INACTIVE,
    PENDING,
    SUSPENDED;

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static CustomerStatus fromJson(String value) {
        return fromString(value);
    }

    public static CustomerStatus fromString(String value) {
        if (value == null) return null;
        String normalized = value.trim().replace(" ", "_").toUpperCase();
        try {
            return CustomerStatus.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
