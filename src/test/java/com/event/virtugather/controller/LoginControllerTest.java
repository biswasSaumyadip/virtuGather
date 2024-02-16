package com.event.virtugather.controller;

import com.event.virtugather.DTO.UserDTO;
import com.event.virtugather.model.User;
import com.event.virtugather.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void testRegisterWithNullUser() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException))
                .andExpect(result -> assertEquals("Required request body is missing: public java.lang.String com.event.virtugather.controller.LoginController.register(com.event.virtugather.DTO.UserDTO)",
                        result.getResolvedException().getMessage()));
    }

    @Test
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

}