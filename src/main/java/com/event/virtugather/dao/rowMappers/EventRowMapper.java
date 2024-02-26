package com.event.virtugather.dao.rowMappers;

import com.event.virtugather.constants.EventType;
import com.event.virtugather.model.VirtuGatherEvent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventRowMapper implements RowMapper<VirtuGatherEvent> {
    @Override
    public VirtuGatherEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
        VirtuGatherEvent event = new VirtuGatherEvent();
        event.setEventId(rs.getInt("event_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setStartTime(rs.getTimestamp("start_time"));
        event.setEndTime(rs.getTimestamp("end_time"));
        event.setLocation(rs.getString("location"));
        event.setEventType(EventType.fromString(rs.getString("event_type")));
        event.setPlatform(rs.getString("platform"));
        event.setCategoryId(rs.getInt("category_id"));
        event.setCreatedBy(rs.getInt("created_by"));
        event.setCreatedAt(rs.getTimestamp("created_at"));
        event.setUpdatedAt(rs.getTimestamp("updated_at"));
        return event;
    }
}