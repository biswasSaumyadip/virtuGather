package com.event.virtugather.dao.Impl;

import com.event.virtugather.constants.SQL.DatabaseQueries;
import com.event.virtugather.dao.EventDao;
import com.event.virtugather.exceptions.DatabaseAccessException;
import com.event.virtugather.exceptions.EventAlreadyExistsException;
import com.event.virtugather.model.VirtuGatherEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventDaoImpl implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int createEvent(VirtuGatherEvent event) throws EventAlreadyExistsException {

        if(event == null){
            throw new IllegalArgumentException("Event cannot be null");
        }

        Object[] params = new Object[] { event.getTitle(), event.getDescription(),
                event.getStartTime(), event.getEndTime(), event.getLocation(), event.getCreatedBy(),
                event.getPlatform(), event.getCategoryId()  };

        try {
            return jdbcTemplate.update(DatabaseQueries.CREATE_EVENT_QUERY, params);
        }catch (DuplicateKeyException exception) {
            throw new EventAlreadyExistsException(event.getTitle(), event.getStartTime().toString(),
                    event.getLocation());
        }catch (DataAccessException e) {
            // throw database access exception
            throw new DatabaseAccessException("Failed to add data due to ", e);
        }
    }
}
