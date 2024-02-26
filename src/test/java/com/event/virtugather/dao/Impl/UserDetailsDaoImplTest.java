package com.event.virtugather.dao.Impl;

import com.event.virtugather.constants.SQL.DatabaseQueries;
import com.event.virtugather.dao.rowMappers.UserDetailsRowMapper;
import com.event.virtugather.exceptions.NotFoundException;
import com.event.virtugather.exceptions.UserDetailsSaveException;
import com.event.virtugather.model.UserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
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

    @Test
    @DisplayName("Test saveUserDetails: when save is successful")
    void testSaveUserDetailsSuccessfully() {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(123L);
        when(jdbcTemplate.update(eq(DatabaseQueries.SAVE_USER_DETAILS_QUERY), any(Object[].class))).thenReturn(1);
        userDetailsDao.saveUserDetails(userDetails);

        verify(jdbcTemplate).update(eq(DatabaseQueries.SAVE_USER_DETAILS_QUERY), any(Object[].class));
    }

    @Test
    @DisplayName("Test saveUserDetails: when an exception occurs")
    void testSaveUserDetailsFailed() {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(123L);
        doThrow(new DataAccessException("Data access exception"){}).when(jdbcTemplate).update(eq(DatabaseQueries.SAVE_USER_DETAILS_QUERY), any(Object[].class));

        assertThrows(UserDetailsSaveException.class, () -> userDetailsDao.saveUserDetails(userDetails));

        verify(jdbcTemplate).update(eq(DatabaseQueries.SAVE_USER_DETAILS_QUERY), any(Object[].class));
    }

    @Test
    @DisplayName("Test updateUserDetails: when save is successful")
    void testUpdateUserDetailsSuccessfully() {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(123L);
        when(jdbcTemplate.update(eq(DatabaseQueries.UPDATE_USER_DETAILS_QUERY), any(Object[].class))).thenReturn(1);
        userDetailsDao.updateUserDetails(userDetails);

        verify(jdbcTemplate).update(eq(DatabaseQueries.UPDATE_USER_DETAILS_QUERY), any(Object[].class));
    }

    @Test
    @DisplayName("Test updateUserDetails: when an exception occurs")
    void testUpdateUserDetailsFailed() {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(123L);
        doThrow(new DataAccessException("Data access exception"){}).when(jdbcTemplate).update(eq(DatabaseQueries.UPDATE_USER_DETAILS_QUERY), any(Object[].class));

        assertThrows(UserDetailsSaveException.class, () -> userDetailsDao.updateUserDetails(userDetails));

        verify(jdbcTemplate).update(eq(DatabaseQueries.UPDATE_USER_DETAILS_QUERY), any(Object[].class));
    }

    @Test
    @DisplayName("Test findByUsername: when user exists")
    public void findByUsername_UserExists_Successful() {
        // Arrange
        UserDetailsDaoImpl userDetailsDao = new UserDetailsDaoImpl(jdbcTemplate);
        UserDetails userDetailsExpected = new UserDetails();
        userDetailsExpected.setFirstName("testUser");
        when(jdbcTemplate.queryForObject(anyString(), any(), any(UserDetailsRowMapper.class)))
                .thenReturn(userDetailsExpected);

        // Act
        UserDetails userDetailsResult = userDetailsDao.findByUsername("testUser");

        // Assert
        assertEquals(userDetailsExpected, userDetailsResult);
    }

    @Test
    @DisplayName("Test findByUsername: when user does not exist")
    public void findByUsername_UserDoesNotExist_ExceptionThrown() {
        // Arrange
        UserDetailsDaoImpl userDetailsDao = new UserDetailsDaoImpl(jdbcTemplate);
        when(jdbcTemplate.queryForObject(anyString(), any(), any(UserDetailsRowMapper.class)))
                .thenThrow(new NotFoundException("User not found"));

        // Assert
        assertThrows(NotFoundException.class, () -> {
            // Act
            userDetailsDao.findByUsername("testUser");
        });
    }

    @Test
    @DisplayName("Test findByUsername: when user does not exist")
    void testFindByUsernameWhenUserDoesNotExist() {
        String username = "username";
        when(jdbcTemplate.queryForObject(
                eq(DatabaseQueries.GET_USER_BY_USERNAME_QUERY),
                eq(new Object[]{username}),
                any(UserDetailsRowMapper.class))
        ).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userDetailsDao.findByUsername(username));
        verify(jdbcTemplate, Mockito.times(1)).queryForObject(
                eq(DatabaseQueries.GET_USER_BY_USERNAME_QUERY),
                eq(new Object[]{username}),
                any(UserDetailsRowMapper.class)
        );
    }

    @Test
    @DisplayName("Test findByUsername: when DataAccessException occurs")
    void testFindByUsernameWhenDataAccessExceptionOccurs() {
        String username = "username";
        when(jdbcTemplate.queryForObject(
                eq(DatabaseQueries.GET_USER_BY_USERNAME_QUERY),
                eq(new Object[]{username}),
                any(UserDetailsRowMapper.class))
        ).thenThrow(new DataIntegrityViolationException("Test exception"));

        assertThrows(NotFoundException.class, () -> userDetailsDao.findByUsername(username));
        verify(jdbcTemplate, times(1)).queryForObject(
                eq(DatabaseQueries.GET_USER_BY_USERNAME_QUERY),
                eq(new Object[]{username}),
                any(UserDetailsRowMapper.class)
        );
    }

    @Test
    @DisplayName("Save profile image success scenario")
    void testSaveProfileImageSuccess() {
        String username = "John";
        String url = "http://example.com/image.jpg";

        when(jdbcTemplate.update(anyString(), eq(username), eq(url))).thenReturn(1);

        int result = userDetailsDao.saveProfileImage(username, url);

        assertEquals(1, result);
        verify(jdbcTemplate).update(anyString(), eq(username), eq(url));
    }

    @Test
    @DisplayName("Save profile image with null values")
    void testSaveProfileImageWithNullValues() {
        assertThrows(NotFoundException.class, () -> userDetailsDao.saveProfileImage(null, null));
        verify(jdbcTemplate, never()).update(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Save profile image when DataAccessException is thrown")
    void testSaveProfileImageDataAccessException() {
        String username = "John";
        String url = "http://example.com/image.jpg";

        when(jdbcTemplate.update(anyString(), anyString(), anyString())).thenThrow(BadSqlGrammarException.class);

        assertThrows(UserDetailsSaveException.class, () -> userDetailsDao.saveProfileImage(username, url));
        verify(jdbcTemplate).update(anyString(), eq(username), eq(url));
    }


}