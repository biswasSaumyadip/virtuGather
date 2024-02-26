package com.event.virtugather.constants;

public enum EventType {
    VIRTUAL,
    PHYSICAL;

    public static EventType fromString(String text) {
        for (EventType type : EventType.values()) {
            if (type.toString().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enumeration constant with text " + text + " found.");
    }

    @Override
    public String toString() {
        return this.name();
    }
}
