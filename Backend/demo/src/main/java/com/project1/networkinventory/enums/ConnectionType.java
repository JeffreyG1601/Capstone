package com.project1.networkinventory.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ConnectionType {
    WIRED,
    WIRELESS;

    @JsonValue
    public String toValue() {
        return name();
    }

    @JsonCreator
    public static ConnectionType fromString(String value) {
        if (value == null) return null;
        String v = value.trim().toUpperCase();
        try {
            return ConnectionType.valueOf(v);
        } catch (IllegalArgumentException e) {
            // optional: return null or rethrow with clearer message
            throw new IllegalArgumentException("Unknown ConnectionType: " + value);
        }
    }
}
