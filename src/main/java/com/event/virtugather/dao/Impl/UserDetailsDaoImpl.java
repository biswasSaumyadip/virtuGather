package com.event.virtugather.dao.Impl;

import com.event.virtugather.constants.SQL.DatabaseQueries;
import com.event.virtugather.dao.UserDetailsDao;
import com.event.virtugather.dao.rowMappers.UserDetailsRowMapper;
import com.event.virtugather.exceptions.InvalidArgumentException;
import com.event.virtugather.exceptions.NotFoundException;
import com.event.virtugather.exceptions.UserDetailsSaveException;
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
            log.info("User details found for id: {}. Details: {}", id, userDetailsList);
            return userDetailsList.get(0);

        } catch (DataAccessException e) {
            log.error("Data access exception occurred while fetching user details for id: " + id, e);
            throw new NotFoundException("Data access exception occurred. Unable to fetch user details for id: " + id);
        }
    }

    @Override
    public int saveUserDetails(UserDetails userDetails) throws UserDetailsSaveException {
        log.info("Saving user details: {}", userDetails);
        try {
            int result = jdbcTemplate.update(DatabaseQueries.SAVE_USER_DETAILS_QUERY, getParams(userDetails));
            log.info("User details saved successfully.");
            return result;
        } catch (DataAccessException e) {
            log.debug("Data access exception occurred while saving user details: " + userDetails, e);
            throw new UserDetailsSaveException("Data access exception occurred. Unable to save user details.", e);
        }
    }

    @Override
    public int updateUserDetails(UserDetails userDetails) {
        log.info("Updating user details: {}", userDetails);
        try {
            Object[] params = getParams(userDetails);
            int result = jdbcTemplate.update(DatabaseQueries.UPDATE_USER_DETAILS_QUERY, params);
            log.info("User details updated successfully.");
            return result;
        } catch (DataAccessException e) {
            log.debug("Data access exception occurred while updating user details: " + userDetails, e);
            throw new UserDetailsSaveException("Data access exception occurred. Unable to save user details.", e);
        }
    }

    @Override
    public UserDetails findByUsername(String username) {
        try {
            UserDetails result = jdbcTemplate.queryForObject(DatabaseQueries.GET_USER_BY_USERNAME_QUERY,  new Object[] { username },   new UserDetailsRowMapper());
            log.info("User details found for username: {}. Details: {}", username, result);
            if(result == null) throw new NotFoundException("User not found with username "+ username);
            return result;
        } catch (DataAccessException e) {
            log.error("Data access exception occurred while finding user details for username: " + username, e);
            throw new NotFoundException("Data access exception occurred. Unable to find user details for username: " + username);
        }
    }

    @Override
    public int saveProfileImage(String username, String URL) {
        log.info("Saving profile image for username: {}", username);
        if(username == null) throw new NotFoundException("Null username not allowed");
        if(URL == null) throw new InvalidArgumentException("Null URL not allowed");
        try {
            int result = jdbcTemplate.update(DatabaseQueries.UPDATE_USER_PROFILE_PICTURE, username, URL);
            log.info("Profile image saved successfully.");
            return result;
        } catch (DataAccessException e) {
            log.debug("Data access exception occurred while saving profile image for username: " + username, e);
            throw new UserDetailsSaveException("Data access exception occurred. Unable to save profile image.", e);
        }
    }

    private Object[] getParams(UserDetails userDetails) {
        log.info("Preparing params for user details: {}", userDetails);
        return new Object[]{
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getPhoneNumber(),
                userDetails.getAddress()};
    }
}
