package com.event.virtugather.DTO;

import com.event.virtugather.model.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private Long detailId;

    private Long user_id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Adapter method to convert DTO to model instance
    public UserDetails toModel() {
        UserDetails userDetails = new UserDetails();
        userDetails.setDetailId(this.detailId);
        userDetails.setUserId(this.user_id);
        userDetails.setFirstName(this.firstName);
        userDetails.setLastName(this.lastName);
        userDetails.setPhoneNumber(this.phoneNumber);
        userDetails.setAddress(this.address);
        userDetails.setCreatedAt(this.createdAt);
        userDetails.setUpdatedAt(this.updatedAt);
        return userDetails;
    }
}
