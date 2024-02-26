package com.event.virtugather.dao;

import com.event.virtugather.exceptions.EventAlreadyExistsException;
import com.event.virtugather.model.VirtuGatherEvent;

public interface EventDao {
    /**
     * Creates an event in the system.
     *
     * @param event the VirtuGatherEvent object containing the details of the event to be created
     * @return the eventId of the created event
     * @throws EventAlreadyExistsException if an event with the same title, event time, and location already exists
     */
    int createEvent(VirtuGatherEvent event) throws EventAlreadyExistsException;

}
