package com.event.virtugather.dao;

import com.event.virtugather.exceptions.UserDetailsSaveException;
import com.event.virtugather.model.UserDetails;

public interface UserDetailsDao {

    /**
     * Retrieves the user details for the given ID.
     *
     * @param id the ID of the user to get the details for
     * @return the UserDetails object containing the user details
     */
    UserDetails getUserDetails(Long id);

    /**
     * Saves the user details for a given user.
     *
     * @param userDetails the UserDetails object containing the user details to save
     */
    int saveUserDetails(UserDetails userDetails) throws UserDetailsSaveException;

    /**
     * Updates the user details for a given user.
     *
     * @param userDetails the UserDetails object containing the updated user details
     */
    int updateUserDetails(UserDetails userDetails) throws UserDetailsSaveException;

    /**
     * Retrieves the user details for the given username.
     *
     * @param username the username of the user to retrieve the details for
     * @return the UserDetails object containing the user details
     */
    UserDetails findByUsername(String username);

    int saveProfileImage(String username, String URL);

}
