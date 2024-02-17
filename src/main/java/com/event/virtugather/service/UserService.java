package com.event.virtugather.service;

import com.event.virtugather.constants.UserField;
import com.event.virtugather.model.User;


public interface UserService {

    int createUser(User user);

    /**
     * Checks if a given username already exists.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean isUsernameExist(String username);

    /**
     * Checks if a given email already exists.
     *
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    boolean isEmailExist(String email);

    /**
     * Updates a specific field of a user identified by the given ID.
     *
     * @param id        the ID of the user to update
     * @param field     the field to update
     * @param newValue  the new value for the field
     * @return the updated value of the field
     */
    String updatedUserField(Long id, UserField field, String newValue);

}
