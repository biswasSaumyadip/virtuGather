package com.event.virtugather.dao;

import com.event.virtugather.model.VirtuGatherEvent;

public interface EventDao {
    /**
     * Creates a new event in the system.
     *
     * @param event the event to be created
     * @return the ID of the created event
     */
    int createEvent(VirtuGatherEvent event);
}
