package com.event.virtugather.dao.Impl;

import com.event.virtugather.dao.EventDao;
import com.event.virtugather.model.VirtuGatherEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventDaoImpl implements EventDao {
    @Override
    public int createEvent(VirtuGatherEvent event) {

        return 0;
    }
}
