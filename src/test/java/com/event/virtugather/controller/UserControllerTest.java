package com.event.virtugather.controller;

import com.event.virtugather.model.UserDetails;
import com.event.virtugather.service.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserDetailsService userDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Test: Get use details and should return User details JSON")
    void testGetUserDetails_ReturnUserDetails() throws Exception {
        long userId = 199L;
        UserDetails userDetails = UserDetails.builder()
                .userId(199L)
                .detailId(198L)
                .firstName("blitz")
                .lastName("lilith")
                .phoneNumber("9905892884")
                .build();

        String expectedObject =
                "{\"detailId\":198,\"userId\":199,\"firstName\":\"blitz\",\"lastName\":\"lilith\",\"phoneNumber\":\"9905892884\",\"address\":null,\"createdAt\":null,\"updatedAt\":null}";

        when(userDetailsService.getUserDetails(userId)).thenReturn(userDetails);

        mockMvc.perform(
                get("/user/"+userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedObject));

        verify(userDetailsService).getUserDetails(userId);
    }

    @Test
    @DisplayName("Test: Create new user details")
    void testCreateUserDetails() throws Exception {
        // Given
        UserDetails userDetails = UserDetails.builder().build();

        // Mock behavior
        when(userDetailsService.createUserDetails(any(UserDetails.class))).thenReturn(1);

        // Convert Object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(userDetails);

        // Perform post request and verify behavior
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(userDetailsService, times(1)).createUserDetails(any(UserDetails.class));
    }

    @Test
    @DisplayName("Test: Update user details with valid user details")
    void updateUserDetails_ValidUserDetails_UserDetailsUpdated() throws Exception {

        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(1L);

        when(userDetailsService.updateUserDetails(userDetails)).thenReturn(1);

        mockMvc.perform(post("/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isOk());
    }


}
