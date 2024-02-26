package com.event.virtugather.exceptions;

public class EventAlreadyExistsException extends RuntimeException {
    public EventAlreadyExistsException(String title, String event_time, String location) {
        super(String.format("Event with the title %s at time %s in location %s already exists", title, event_time, location));
    }
}