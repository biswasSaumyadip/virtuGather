package com.event.virtugather.controller;

import com.event.virtugather.DTO.UserDTO;
import com.event.virtugather.model.User;
import com.event.virtugather.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


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
        User user = userDTO.toUser();
        Mockito.when(userService.createUser(any(User.class))).thenReturn(1);

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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mockMvc.perform(post("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(null)))
                    .andExpect(status().isBadRequest());
        });

        String expectedMessage = NULL_USER_MSG;
        String actualMessage = exception.getMessage();

        assertThat(expectedMessage).isEqualTo(actualMessage);
    }
}