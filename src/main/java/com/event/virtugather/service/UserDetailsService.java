package com.event.virtugather.service;

import com.event.virtugather.model.UserDetails;

public interface UserDetailsService {

    /**
     * Creates user details for a given user.
     *
     * @param userDetails the UserDetails object containing the user details to create
     * @return an integer value representing the result of the operation
     */
    int createUserDetails(UserDetails userDetails);

    /**
     * Updates the details of a user.
     *
     * @param userDetails the UserDetails object containing the updated user details
     * @return an integer value representing the result of the operation:
     *         1 if the update was successful
     */
    int updateUserDetails(UserDetails userDetails);


    /**
     * Retrieves the user details for a given user ID.
     *
     * @param id the ID of the user
     * @return the UserDetails object containing the user details
     */
    UserDetails getUserDetails(long id);
}
