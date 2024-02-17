package com.event.virtugather.dao.Impl;

import com.event.virtugather.constants.SQL.DatabaseQueries;
import com.event.virtugather.dao.UserDetailsDao;
import com.event.virtugather.exceptions.NotFoundException;
import com.event.virtugather.model.UserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDetailsDaoImpl implements UserDetailsDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails getUserDetails(Long id) {
        log.info("Fetching user details for id: {}", id);
        try {
            List<UserDetails> userDetailsList
                    = jdbcTemplate.query(
                    DatabaseQueries.GET_USER_DETAILS_QUERY,
                    new BeanPropertyRowMapper<>(UserDetails.class),
                    id);
            log.info("User details found for id: {}", id);
            return userDetailsList.get(0);

        } catch (DataAccessException e) {
            log.error("Data access exception occurred while fetching user details for id: " + id, e);
            throw new NotFoundException("Data access exception occurred. Unable to fetch user details for id: " + id);
        }
    }
}
