package com.event.virtugather.model;

import com.event.virtugather.constants.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirtuGatherEvent {
    private int eventId;
    private String title;
    private String description;
    private Timestamp startTime;
    private Timestamp endTime;
    private String location;
    private EventType eventType;
    private String platform;
    private int categoryId;
    private int createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}