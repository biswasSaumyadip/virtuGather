package com.event.virtugather.service;

import com.event.virtugather.model.User;

import java.util.Optional;

public interface UserService {

    int createUser(User user);

    /**
     * Checks if a given username already exists.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean isUsernameExist(String username);

}
