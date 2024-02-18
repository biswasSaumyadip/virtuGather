package com.event.virtugather.controller;

import com.event.virtugather.model.UserDetails;
import com.event.virtugather.service.UserDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;
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


}
