package com.event.virtugather.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long user_id;

    @NotEmpty(message = "Username may not be empty")
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @Setter(AccessLevel.NONE)
    private String password;


    private Timestamp created_at;
    private Timestamp updated_at;

    public void securePassword(String password) {
        this.password = password;
    }

}
