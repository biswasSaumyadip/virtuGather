package com.event.virtugather.controller;

import com.event.virtugather.DTO.UserDTO;
import com.event.virtugather.constants.UserField;
import com.event.virtugather.model.User;
import com.event.virtugather.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = LoginController.class)
public class LoginControllerTest {

    private static final String NULL_USER_MSG = "User cannot be null";
    private static final String USER_REG_MSG = "User registered with ID: ";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should register user")
    public void testRegister() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(userService.createUser(any(User.class))).thenReturn(1);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    var response = result.getResponse().getContentAsString();
                    assertThat(response).isEqualTo(USER_REG_MSG + 1);
                });
    }

    @Test
    @DisplayName("Given a DuplicateKeyException, when register is called, then return HttpStatus.CONFLICT")
    public void givenDuplicateKeyException_whenRegister_thenReturnsHttpStatusConflict() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPassword");

        ObjectMapper objectMapper = new ObjectMapper();
        String userDTOJson = objectMapper.writeValueAsString(userDTO);

        when(userService.createUser(any(User.class))).thenThrow(DuplicateKeyException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJson))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("Should handle null user registration")
    public void testRegisterWithNullUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException))
                .andExpect(result -> assertEquals("Required request body is missing: public java.lang.String com.event.virtugather.controller.LoginController.register(com.event.virtugather.DTO.UserDTO) throws org.springframework.dao.DuplicateKeyException",
                        result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("Should check if username exists")
    void checkUsernameTest() throws Exception {
        String username = "testusername";

        // Given
        given(userService.isUsernameExist(username)).willReturn(true);

        // When & Then
        mockMvc.perform(get("/checkUsername/" + username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify interactions
        verify(userService).isUsernameExist(username);
    }

    @Test
    @DisplayName("Should update username")
    void shouldUpdateUsername() throws Exception {
        // Arrange
        Long id = 1L;
        UserField field = UserField.USERNAME;
        String newValue = "newUsername";

        // mock behavior
        when(userService.updatedUserField(id, field, newValue)).thenReturn("Username updated");

        // Act & Assert
        mockMvc.perform(post("/updateUserField/{id}", id)
                        .param("field", field.name())
                        .param("newValue", newValue))
                .andExpect(status().isOk())
                .andExpect(content().string("Username updated"));

        // Verify
        verify(userService).updatedUserField(id, field, newValue);
    }

    @Test
    @DisplayName("Should update email")
    void shouldUpdateEmail() throws Exception {
        // Arrange
        Long id = 1L;
        UserField field = UserField.EMAIL;
        String newValue = "newEmail@example.com";

        // mock behavior
        when(userService.updatedUserField(id, field, newValue)).thenReturn("Email updated");

        // Act & Assert
        mockMvc.perform(post("/updateUserField/{id}", id)
                        .param("field", field.name())
                        .param("newValue", newValue))
                .andExpect(status().isOk())
                .andExpect(content().string("Email updated"));

        // Verify
        verify(userService).updatedUserField(id, field, newValue);
    }

    @Test
    @DisplayName("Should update password")
    void shouldUpdatePassword() throws Exception {
        // Arrange
        Long id = 1L;
        UserField field = UserField.PASSWORD;
        String newValue = "newPassword";

        // mock behavior
        when(userService.updatedUserField(id, field, newValue)).thenReturn("Password updated");

        // Act & Assert
        mockMvc.perform(post("/updateUserField/{id}", id)
                        .param("field", field.name())
                        .param("newValue", newValue))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated"));

        // Verify
        verify(userService).updatedUserField(id, field, newValue);
    }

    @Test
    @DisplayName("When Email Does not exist, return false")
    public void checkEmailDoesNotExist() throws Exception {
        String email = "email@domain.com";
        when(userService.isEmailExist(email)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/checkEmail/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("When Email Exists, return true")
    public void checkEmailExists() throws Exception {
        String email = "existingEmail@domain.com";
        when(userService.isEmailExist(email)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/checkEmail/{email}", email))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

}