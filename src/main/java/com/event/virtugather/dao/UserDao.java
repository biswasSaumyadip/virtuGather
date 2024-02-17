package com.event.virtugather.dao;

import com.event.virtugather.constants.UserField;
import com.event.virtugather.model.User;

import java.util.Optional;

/**
 * UserDao is an interface that defines the operations for interacting with the user table in the database.
 */
public interface UserDao {

    /**
     * Creates a new user in the database.
     *
     * @param user The User object containing the user information.
     * @return The ID of the newly created user.
     */
    int createUser(User user);

    /**
     * Retrieves a user from the database based on their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return An Optional containing the retrieved user if found, or an empty Optional if the user does not exist or an error occurred.
     */
    Optional<User> getUserBy(Long id);

    /**
     * Updates a user in the database.
     *
     * @param user The User object containing the updated user information.
     * @return The number of rows affected by the update operation.
     */
    int updateUser(User user);

    /**
     * Deletes a user from the database based on their ID.
     *
     * @param id The ID of the user to delete.
     * @return The number of rows affected by the delete operation.
     */
    int deleteUser(Long id);

    /**
     * Validates a user by checking if the provided username and password match with a user in the database.
     *
     * @param username The username of the user to validate.
     * @param password The password of the user to validate.
     * @return An Optional containing the validated user if found, or an empty Optional if the user is not valid or an error occurred.
     */
    Optional<User> validateUser(String username, String password);

    /**
     * Checks if a username exists in the database.
     *
     * @param username The username to check.
     * @return true if the username exists, false otherwise.
     */
    boolean isUsernameExist(String username);

    /**
     * Checks if an email exists in the database.
     *
     * @param email The email to check.
     * @return true if the email exists, false otherwise.
     */
    boolean isEmailExist(String email);

    /**
     * Updates a specific field of an user in the database.
     *
     * @param id The ID of the user to update.
     * @param field The field of the user to update.
     * @param newValue The new value for the specified field.
     * @return The number of rows affected by the update operation.
     */
    int updateUserField(Long id, UserField field, String newValue);

}
