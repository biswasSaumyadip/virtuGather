package com.event.virtugather.dao.rowMappers;

import com.event.virtugather.model.UserDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDetailsRowMapper implements RowMapper<UserDetails> {

    @Override
    public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserDetails.builder()
                .detailId(rs.getLong("detailId"))
                .userId(rs.getLong("userId"))
                .firstName(rs.getString("firstName"))
                .lastName(rs.getString("lastName"))
                .phoneNumber(rs.getString("phoneNumber"))
                .address(rs.getString("address"))
                .createdAt(rs.getTimestamp("createdAt"))
                .updatedAt(rs.getTimestamp("updatedAt"))
                .build();
    }
}