package com.event.virtugather.dao.unit;

import com.event.virtugather.constants.UserField;
import com.event.virtugather.dao.Impl.UserDaoImplementation;
import com.event.virtugather.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserDaoImplementation userDao; //mock objects will be injected in @injectMocks

    @Test
    @DisplayName("Testing if user creation method works")
    void createUserTest() {

        User user = User.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password123")
                .build();

        // Mock the jdbcTemplate to be used in test

        // Stub the mock jdbcTemplate's update method to modify the keyHolder it receives as an argument.
        // This simulates the key generation expected to happen in the real jdbcTemplate.update method.
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            KeyHolder keyHolder = (KeyHolder) args[1];

            // Create list with one generated key
            List<Map<String, Object>> keys = new ArrayList<>();
            Map<String, Object> key = new HashMap<>();
            key.put("GENERATED_KEY", 1);
            keys.add(key);

            // Use reflection to insert our generated keys into the keyHolder
            Field field = GeneratedKeyHolder.class.getDeclaredField("keyList");
            field.setAccessible(true);
            field.set(keyHolder, keys);

            return null;
        }).when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        // Now perform the test with the correctly stubbed jdbcTemplate
        userDao.createUser(user);

        verify(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    @DisplayName("Test for Successful User Creation")
    void createUserTest_Success() {
        User user = User.builder()
                .user_id(1)
                .username("testUser")
                .email("test@example.com")
                .password("password123")
                .build();

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer((Answer<Integer>) invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(Map.of("id", 1));
            return 1;
        });

        int result = userDao.createUser(user);

        assertEquals(1, result, "Expected createUser to return 1 on success");
        verify(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    @DisplayName("Test for Duplicate User Creation")
    void createUserTest_duplicate() {
        // Given
        User user = User.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password123")
                .build();

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenThrow(new DuplicateKeyException("Duplicate Key"));

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> userDao.createUser(user),
                "Expected a RuntimeException to be thrown");

        // Then
        assertEquals("Error occurred while creating user", exception.getMessage());
        verify(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    @DisplayName("Test for Create user database connectivity issue")
    void createUserTest_dataException() {
        User user = User.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password123")
                .build();

        doThrow(new DataAccessException("Database connectivity issue") {})
                .when(jdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));

        // check that RuntimeException is actually thrown
        Exception exception = assertThrows(RuntimeException.class, () -> userDao.createUser(user));
        assertEquals("Error occurred while creating user", exception.getMessage());
    }
    @Test
    @DisplayName("Test for Retrieving Generated ID During User Creation")
    void createUserAndRetrieveGeneratedId() {
        User user = User.builder()
                .user_id(1)
                .username("testUser")
                .email("test@example.com")
                .password("password123")
                .build();

        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenAnswer((Answer<Integer>) answer->{
                    KeyHolder keyHolder = answer.getArgument(1);
                    keyHolder.getKeyList().add(Map.of("id", 2));

                    return 1;
                });

        int result = userDao.createUser(user);

        assertTrue(result > 0, "Generated ID should be greater than 0");

    }

    @Test
    @DisplayName("Test for Fetching User by ID")
    void getUserBy_UUID_ShouldReturnUser() {
        String sql = "SELECT user_id, username, password, email, created_at, updated_at FROM " +
                "users WHERE user_id = ?";

        Long id = 1L;
        User user = User.builder()
                .user_id(id)
                .username("testUser")
                .email("test@example.com")
                .password("password123")
                .build();


        when(jdbcTemplate.query(eq(sql), any(ResultSetExtractor.class), eq(id)))
                .thenAnswer(invocation -> {
                    ResultSetExtractor<User> extractor = invocation.getArgument(1);
                    // Simulate ResultSet behavior here
                    ResultSet rs = mock(ResultSet.class);
                    when(rs.next()).thenReturn(true); // Simulate that a user is found
                    // Mock other rs.getXXX methods to return values
                    when(rs.getLong("user_id")).thenReturn(user.getUser_id());
                    when(rs.getString("username")).thenReturn(user.getUsername());
                    when(rs.getString("password")).thenReturn(user.getPassword());
                    when(rs.getString("email")).thenReturn(user.getEmail());
                    // etc. for other fields
                    return extractor.extractData(rs); // Use the extractor with the mocked ResultSet
                });

        Optional<User> result = userDao.getUserBy(id);


        assertTrue(result.isPresent(), "Expected a non-empty Optional for a valid user ID");
        assertEquals(user, result.get(), "Expected the retrieved user to match the mocked one");
    }

    @Test
    @DisplayName("Test for Fetching User by Non-existent ID")
    void  getUserBy_NonExistentId_ShouldReturnEmptyOptional(){
        String sql = "SELECT user_id, username, password, email, created_at, updated_at FROM " +
                "users WHERE user_id = ?";

        Long id = 1L;

        when(jdbcTemplate.query(eq(sql), any(ResultSetExtractor.class), eq(id)))
                .thenAnswer(invocationOnMock -> null);

        Optional<User> result = userDao.getUserBy(id);

        assertTrue(result.isEmpty(), "Expect an empty result");

    }

    @Test
    @DisplayName("Test for Handling Database Error on Fetch User by ID")
    void getUserBy_DatabaseError_ShouldHandleGracefully() {
        String sql = "SELECT user_id, username, password, email, created_at, updated_at FROM users WHERE user_id = ?";

        Long id = 1L;

        // Mock jdbcTemplate to throw a DataAccessException when the query method is called
        when(jdbcTemplate.query(eq(sql), any(ResultSetExtractor.class), eq(id)))
                .thenThrow(new DataAccessException("Database error") {
                });

        // When
        Optional<User> result = userDao.getUserBy(id);

        // Then
        assertTrue(result.isEmpty(), "Expect an empty result for database error");
        verify(jdbcTemplate).query(eq(sql), any(ResultSetExtractor.class), eq(id));
    }


    @Test
    @DisplayName("Test for Handling Null ID on Fetch User by ID")
    void getUserBy_NullId_ShouldHandleGracefullyTest() {
        // Given
        String sql = "SELECT user_id, username, password, email, created_at, updated_at FROM users WHERE user_id = ?";

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userDao.getUserBy(null),
                "Expected IllegalArgumentException for user with null ID");

        // Then
        assertEquals("User ID cannot be null", exception.getMessage());
        verify(jdbcTemplate, times(0)).query(eq(sql), any(ResultSetExtractor.class), eq(null));
    }

    @Test
    @DisplayName("Test for Updating User")
    public void updateUser_NormalCase_Success()  {
        // Arrange
       User user = User.builder()
                .user_id(100L)
                .username("testUser")
                .email("test@example.com")
                .password("password123")
                .build();

        when(jdbcTemplate.update(any(PreparedStatementCreator.class))).thenReturn(1);

        // Act
        int result = userDao.updateUser(user);

        // Assert
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test for IllegalArgumentException on Empty User Update")
    public void updateUser_NullUserPassed_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> userDao.updateUser(null));
    }

    @Test
    @DisplayName("Test for DuplicateKeyException on User Update")
    public void updateUser_DuplicateKeyIsThrown_RuntimeExceptionThrown() {
        // Arrange
        User user = User.builder()
                .user_id(500L)
                .username("testUser")
                .email("test@example.com")
                .password("password123")
                .build();

        when(jdbcTemplate.update(any(PreparedStatementCreator.class))).thenThrow(new DuplicateKeyException("Duplicate key"));

        // Assert
        Exception exception = assertThrows(RuntimeException.class, () -> userDao.updateUser(user),
                "Expected a RuntimeException to be thrown");

        // Act
        assertEquals("Error occurred while updating user", exception.getMessage());
    }

    @Test
    @DisplayName("Test for Successful User Deletion")
    void deleteUserTest_Success() {

        String sql = "DELETE FROM users WHERE user_id = ?";

        Long id = 1L;

        when(jdbcTemplate.update(sql, id)).thenReturn(1);

        int result = userDao.deleteUser(id);

        assertEquals(1, result, "Expected deleteUser to return 1 on success");
        verify(jdbcTemplate).update(sql, id);
    }

    @Test
    @DisplayName("Test for Non-Existent User Deletion")
    void deleteUserTest_NonExistentUser() {

        String sql = "DELETE FROM users WHERE user_id = ?";

        Long id = 1L;

        when(jdbcTemplate.update(sql, id)).thenReturn(0);

        int result = userDao.deleteUser(id);

        assertEquals(0, result, "Expected deleteUser for non-existent user to return 0");
        verify(jdbcTemplate).update(sql, id);
    }

    @Test
    @DisplayName("Test for Exception During User Deletion")
    void deleteUserTest_Exception() {

        Long id = 1L;
        final String QUERY_DELETE_USER = "DELETE FROM users WHERE user_id = ?";
        final String ERROR_WHILE_DELETING_USER = "Error occurred while deleting user";

        doThrow(new UncategorizedDataAccessException("Test exception", null) {})
                .when(jdbcTemplate).update(QUERY_DELETE_USER, id);

        Exception exception = assertThrows(RuntimeException.class, () -> userDao.deleteUser(id),
                "Expected RuntimeException to be thrown");

        assertEquals(ERROR_WHILE_DELETING_USER, exception.getMessage());
        verify(jdbcTemplate).update(QUERY_DELETE_USER, id);
    }

    @Test
    @DisplayName("Test for Valid User Check by Username and Password")
    void validateUser_withValidCredentials() {

        User user = User.builder()
                .username("testUser")
                .password("password123")
                .build();

        String sql = "SELECT user_id, username, password, email, created_at, updated_at FROM users WHERE username = ? AND password = ?";

        when(jdbcTemplate.query(eq(sql), any(ResultSetExtractor.class), eq(user.getUsername()), eq(user.getPassword())))
                .thenAnswer(invocation -> {
                    ResultSetExtractor<User> extractor = invocation.getArgument(1);
                    // Simulating ResultSet behavior here
                    ResultSet rs = mock(ResultSet.class);
                    when(rs.next()).thenReturn(true); // Simulate that a user is found
                    // Mocking other rs.getXXX methods to return values
                    when(rs.getString("username")).thenReturn(user.getUsername());
                    when(rs.getString("password")).thenReturn(user.getPassword());
                    // etc. for other fields
                    return extractor.extractData(rs); // Using the extractor with the mocked ResultSet
                });

        Optional<User> result = userDao.validateUser(user.getUsername(), user.getPassword());


        assertTrue(result.isPresent(), "Expected User to be valid for correct username and password");
    }


    @Test
    @DisplayName("Testing for username existence when username exists")
    void testIsUsernameExist_whenUsernameExists() {
        String username = "existingUser";
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(username))).thenReturn(1);

        boolean exists = userDao.isUsernameExist(username);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Testing for username existence when username doesn't exist")
    void testIsUsernameExist_whenUsernameDoesNotExist() {
        String username = "nonExistingUser";
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(username))).thenReturn(0);

        boolean exists = userDao.isUsernameExist(username);

        assertFalse(exists);
    }

    @Test
    @DisplayName("Testing for username existence when empty data exception is thrown")
    void testIsUsernameExist_whenEmpty_resultDataAccessExceptionIsThrown() {
        String username = "testUser";
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(username)))
                .thenThrow(new EmptyResultDataAccessException(1));

        boolean exists = userDao.isUsernameExist(username);

        assertFalse(exists);
    }

    @Test
    public void testUpdateUsernameField() {
        testUpdateUserField(UserField.USERNAME, "new-username");
    }

    @Test
    public void testUpdateEmailField() {
        testUpdateUserField(UserField.EMAIL, "new-email@example.com");
    }

    @Test
    public void testUpdatePasswordField() {
        testUpdateUserField(UserField.PASSWORD, "new-secret-password");
    }

    @Test
    public void testUpdateNonexistentUserField() {
        when(userDao.updateUserField(Mockito.anyLong(), Mockito.any(), Mockito.anyString())).thenReturn(0);
        int result = userDao.updateUserField(9999L, UserField.USERNAME, "new-username");

        Mockito.verify(userDao, Mockito.times(1)).updateUserField(any(Long.class), any(UserField.class), any(String.class));
        Assertions.assertEquals(0, result, "Updating field of non-existent user should return 0");
    }

    private void testUpdateUserField(UserField userField, String newValue) {
        when(userDao.updateUserField(anyLong(), any(), anyString())).thenReturn(1);

        int result = userDao.updateUserField(1L, userField, newValue);

        Mockito.verify(userDao, Mockito.times(1)).updateUserField(any(Long.class), any(UserField.class), any(String.class));
        Assertions.assertTrue(result > 0, "User field update failed");
    }
}
