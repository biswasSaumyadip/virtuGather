package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long user_id;
    private String username;
    private String email;

    @Setter(AccessLevel.NONE)
    private String password;

    private Timestamp created_at;
    private Timestamp updated_at;

}
