package com.event.virtugather.DTO;

import com.event.virtugather.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String password;

    public User toUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setEmail(this.email);
        return user;
    }
}
