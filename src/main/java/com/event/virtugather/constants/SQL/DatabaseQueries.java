package com.event.virtugather.constants.SQL;

public class DatabaseQueries {
    public static final String QUERY_CREATE_USER = "INSERT INTO users(username, email, password) VALUES (?,?,?)";
    public static final String QUERY_GET_USER = "SELECT user_id, username, password, email, created_at, updated_at FROM users WHERE user_id = ?";
    public static final String QUERY_CHECK_USERNAME = "SELECT COUNT(1) FROM users WHERE username = ?";
    public static final String QUERY_GET_USER_BY_USERNAME_PASSWORD = "SELECT user_id, username, password, email, created_at, updated_at FROM users WHERE username = ? AND password = ?";
    public static final String QUERY_UPDATE_USER = "UPDATE users SET username = ?, email = ?, password = ? WHERE user_id = ?";
    public static final String QUERY_DELETE_USER = "DELETE FROM users WHERE user_id = ?";
    public static final String QUERY_CHECK_EMAIL = "SELECT COUNT(1) FROM users WHERE email = ?";

    public final static String GET_USER_DETAILS_QUERY = "SELECT detail_id, user_id, first_name, last_name, " +
            "phone_number, address, created_at, updated_at " +
            "FROM user_details WHERE user_id = ?";

    public static final String UPDATE_USER_DETAILS_QUERY  = "UPDATE user_details " +
            "SET first_name = ?, last_name = ?, phone_number = ?, address = ? " +
            "WHERE user_id = ?";

    public static final String SAVE_USER_DETAILS_QUERY = "INSERT INTO user_details " +
            "(user_id,first_name, last_name, phone_number, address) VALUE (?,?,?,?,?)";
}
