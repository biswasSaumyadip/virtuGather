package com.event.virtugather.dao.Impl;

import com.event.virtugather.constants.SQL.DatabaseQueries;
import com.event.virtugather.constants.UserField;
import com.event.virtugather.dao.UserDao;
import com.event.virtugather.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImplementation implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImplementation.class);

    private static final String USER_CREATION_SUCCESSFUL = "User creation successful";
    private static final String USER_UPDATE_SUCCESSFUL = "User update successful";
    private static final String USER_DELETION_SUCCESSFUL = "User deletion successful";
    private static final String ERROR_WHILE_CREATING_USER = "Error occurred while creating user";
    private static final String ERROR_WHILE_UPDATING_USER = "Error occurred while updating user";
    private static final String ERROR_WHILE_DELETING_USER = "Error occurred while deleting user";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int createUser(User user) {
        isValidUser(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(setParametersForCreateUser(user), keyHolder);
            LOGGER.info(USER_CREATION_SUCCESSFUL);
            return Optional.ofNullable(keyHolder.getKey()).orElseThrow(RuntimeException::new).intValue();
        } catch (DataAccessException exception) {
            LOGGER.error(String.format("%s, due to - %s", ERROR_WHILE_CREATING_USER, exception.getMessage()));
            throw new RuntimeException(ERROR_WHILE_CREATING_USER, exception);
        }
    }

    @Override
    public int updateUser(User user) {
        isValidUser(user);
        try {
            int rows = jdbcTemplate.update(setParametersForUpdateUser(user));
            LOGGER.info(USER_UPDATE_SUCCESSFUL);
            return rows;
        } catch (DataAccessException exception) {
            LOGGER.error(String.format("%s, due to - %s", ERROR_WHILE_UPDATING_USER, exception.getMessage()));
            throw new RuntimeException(ERROR_WHILE_UPDATING_USER, exception);
        }
    }

    @Override
    public int deleteUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        try {
            int rows = jdbcTemplate.update(DatabaseQueries.QUERY_DELETE_USER, id);
            LOGGER.info(USER_DELETION_SUCCESSFUL);
            return rows;
        } catch (DataAccessException exception) {
            LOGGER.error(String.format("%s, due to - %s", ERROR_WHILE_DELETING_USER, exception.getMessage()));
            throw new RuntimeException(ERROR_WHILE_DELETING_USER, exception);
        }
    }

    @Override
    public Optional<User> getUserBy(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        try {
            return Optional.ofNullable(jdbcTemplate.query(DatabaseQueries.QUERY_GET_USER, this::buildUserFromResultSet, id));
        } catch (Exception exception) {
            LOGGER.error("Unable to fetch user data, due to - {}", exception.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> validateUser(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and Password cannot be null");
        }

        try {
            return Optional.ofNullable(
                    jdbcTemplate.query(
                            DatabaseQueries.QUERY_GET_USER_BY_USERNAME_PASSWORD,
                            this::buildUserFromResultSet,
                            username,
                            password)
            );

        } catch (Exception exception) {
            LOGGER.error("Unable to validate user credentials, due to - {}", exception.getLocalizedMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean isUsernameExist(String username) {
        try {
            Integer count = jdbcTemplate.queryForObject(DatabaseQueries.QUERY_CHECK_USERNAME, Integer.class, username);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean isEmailExist(String email) {
        try {
            Integer count = jdbcTemplate.queryForObject(DatabaseQueries.QUERY_CHECK_EMAIL, Integer.class, email);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }

    }

    @Override
    public int updateUserField(Long id, UserField field, String newValue) {
        String columnName = getColumnName(field);

        String sqlQuery = String.format("UPDATE user SET %s = :newValue WHERE user_id = :userId", columnName);

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("newValue", newValue);
        parameters.addValue("userId", id);

        return jdbcTemplate.update(sqlQuery, parameters);
    }

    private String getColumnName(UserField field) {
        return switch (field) {
            case EMAIL -> "email";
            case USERNAME -> "username";
            case PASSWORD -> "password";
        };
    }

    private void isValidUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    private PreparedStatementCreator setParametersForCreateUser(User user) {
        return getPreparedStatementCreator(user, DatabaseQueries.QUERY_CREATE_USER);
    }

    private PreparedStatementCreator getPreparedStatementCreator(User user, String queryCreateUser) {
        return connection -> {
            PreparedStatement ps = connection.prepareStatement(queryCreateUser, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getPassword());
                return ps;

        };
    }

    private PreparedStatementCreator setParametersForUpdateUser(User user) {
        return getPreparedStatementCreator(user, DatabaseQueries.QUERY_UPDATE_USER);
    }

    private User buildUserFromResultSet(ResultSet rs) throws SQLException {
        return User.builder()
                .user_id(rs.getLong("user_id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .email(rs.getString("email"))
                .created_at(rs.getTimestamp("created_at")).updated_at(rs.getTimestamp("updated_at"))
                .build();
    }
}
