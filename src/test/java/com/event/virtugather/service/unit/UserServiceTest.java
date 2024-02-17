package com.event.virtugather.service.unit;

import com.event.virtugather.constants.UserField;
import com.event.virtugather.dao.UserDao;
import com.event.virtugather.model.User;
import com.event.virtugather.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Test creating user returns correct result")
    public void testCreateUser() {
        User testUser = new User();
        // set user details

        when(userDao.createUser(testUser)).thenReturn(1);

        int result = userService.createUser(testUser);

        assertEquals(1, result, "Expected createUser to return the user ID");
        verify(userDao).createUser(testUser);
    }

    @Test
    @DisplayName("Test creating user with null throws exception")
    public void testCreateUser_NullUser() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Test creating user propagates exceptions correctly")
    public void testCreateUser_ExceptionPropagation() {
        User testUser = new User();
        when(userDao.createUser(testUser)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> userService.createUser(testUser));
    }

    @Test
    @DisplayName("Test if username exists")
    public void testIsUsernameExist() {
        String existingUsername = "existingUser";

        // Set expectations: the username does exist.
        when(userService.isUsernameExist(existingUsername)).thenReturn(true);

        // Test the method.
        boolean isExist = userService.isUsernameExist(existingUsername);

        // Assert that the username does exist.
        Assertions.assertTrue(isExist, "Username should exist.");
    }

    @Test
    @DisplayName("Test if username does not exist")
    public void testIsUsernameNotExist() {
        String nonExistingUsername = "nonExistingUser";

        // Set expectations: the username does not exist.
        when(userService.isUsernameExist(nonExistingUsername)).thenReturn(false);

        // Tests the method.
        boolean isExist = userService.isUsernameExist(nonExistingUsername);

        // Assert that the username does not exist.
        Assertions.assertFalse(isExist, "Username should not exist.");
    }

    @Test
    @DisplayName("Test updating user field successfully")
    void shouldUpdateUserField() {
        Long userId = 1L;
        UserField field = UserField.USERNAME;
        String newValue = "newValue";

        when(userDao.updateUserField(userId, field, newValue)).thenReturn(1);

        String result = userService.updatedUserField(userId, field, newValue);

        assertEquals("User field updated successfully", result);
    }

    @Test
    @DisplayName("Test updating user field with invalid parameters")
    void shouldNotUpdateUserFieldWithInvalidParameters() {
        Long userId = 1L;
        UserField field = UserField.USERNAME;
        String newValue = "newValue";

        when(userDao.updateUserField(userId, field, newValue)).thenReturn(0);

        assertThrows(IllegalArgumentException.class, () -> userService.updatedUserField(userId, field, newValue));
    }
}
