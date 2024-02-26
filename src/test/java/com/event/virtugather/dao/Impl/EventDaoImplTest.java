package com.event.virtugather.dao.Impl;

import com.event.virtugather.constants.SQL.DatabaseQueries;
import com.event.virtugather.exceptions.DatabaseAccessException;
import com.event.virtugather.exceptions.EventAlreadyExistsException;
import com.event.virtugather.model.VirtuGatherEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private EventDaoImpl eventDaoImpl;

    @BeforeEach
    void setUp() {
        // Initialize logic here if needed
    }

    @Test
    @DisplayName("Test createEvent success scenario")
    void createEvent_SuccessScenario() {
        VirtuGatherEvent event = new VirtuGatherEvent(); // use correct event creation here
        // use this when database interaction is success

        when(jdbcTemplate.update(eq(DatabaseQueries.CREATE_EVENT_QUERY), any(Object[].class))).thenReturn(1);
        assertDoesNotThrow(() -> eventDaoImpl.createEvent(event));
    }

    @Test
    @DisplayName("Test createEvent failure scenario: EventAlreadyExistsException")
    void createEvent_EventAlreadyExist() {
        VirtuGatherEvent event = new VirtuGatherEvent(); // use obvious exist event here
        event.setTitle("Existing Event Title");
        event.setStartTime(new Timestamp(74846111));
        event.setLocation("Existing Event Location");

        when(jdbcTemplate.update(eq(DatabaseQueries.CREATE_EVENT_QUERY), any(Object[].class)))
                .thenThrow(DuplicateKeyException.class);
        assertThrows(EventAlreadyExistsException.class, () -> eventDaoImpl.createEvent(event));
    }

    @Test
    @DisplayName("Test createEvent failure scenario: Event object is null")
    void createEvent_NullEvent() {
        assertThrows(IllegalArgumentException.class, () -> eventDaoImpl.createEvent(null));
    }

    @Test
    @DisplayName("Test createEvent failure scenario: DataIntegrityViolationException")
    void createEvent_DataIntegrityViolationException() {
        VirtuGatherEvent event = new VirtuGatherEvent(); // use correct event creation here
        when(jdbcTemplate.update(eq(DatabaseQueries.CREATE_EVENT_QUERY), any(Object[].class)))
                .thenThrow(DataIntegrityViolationException.class);
        assertThrows(DatabaseAccessException.class, () -> eventDaoImpl.createEvent(event));
    }

}