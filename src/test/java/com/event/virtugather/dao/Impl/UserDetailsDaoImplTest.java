package com.event.virtugather.dao.Impl;

import com.event.virtugather.constants.SQL.DatabaseQueries;
import com.event.virtugather.exceptions.NotFoundException;
import com.event.virtugather.model.UserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class UserDetailsDaoImplTest {

    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    UserDetailsDaoImpl userDetailsDao;

    @Test
    @DisplayName("Test getUserDetails: when user details exist")
    public void testGetUserDetails_whenUserExists() {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(123L);
        when(jdbcTemplate.query(eq(DatabaseQueries.GET_USER_DETAILS_QUERY),
                any(BeanPropertyRowMapper.class), eq(123L)))
                .thenReturn(Collections.singletonList(userDetails));

        UserDetails result = userDetailsDao.getUserDetails(123L);

        assertEquals(userDetails, result);
        verify(jdbcTemplate).query(eq(DatabaseQueries.GET_USER_DETAILS_QUERY),
                any(BeanPropertyRowMapper.class),
                eq(123L));
    }

    @Test
    @DisplayName("Test getUserDetails: when user details do not exist")
    public void testGetUserDetails_whenUserNotExists() {
        when(jdbcTemplate.query(any(String.class), 
                any(BeanPropertyRowMapper.class), anyLong()))
                .thenThrow(EmptyResultDataAccessException.class);

        assertThrows(NotFoundException.class, () -> userDetailsDao.getUserDetails(123L));
        verify(jdbcTemplate).query(eq(DatabaseQueries.GET_USER_DETAILS_QUERY),
                any(BeanPropertyRowMapper.class),
                eq(123L));
    }

}