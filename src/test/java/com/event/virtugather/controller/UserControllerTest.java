package com.event.virtugather.controller;

import com.event.virtugather.exceptions.NotFoundException;
import com.event.virtugather.model.UserDetails;
import com.event.virtugather.service.ImageService;
import com.event.virtugather.service.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImageService imageService;

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
                "{\"detailId\":198,\"userId\":199,\"firstName\":\"blitz\",\"lastName\":\"lilith\",\"imageUrl\":null,\"phoneNumber\":\"9905892884\",\"address\":null,\"createdAt\":null,\"updatedAt\":null}";

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

    @Test
    public void getUserDetails_ValidId_ReturnsUserDetails() throws Exception {
        long validId = 1;
        UserDetails userDetails = UserDetails.builder()
                .userId(199L)
                .detailId(198L)
                .firstName("blitz")
                .lastName("lilith")
                .phoneNumber("9905892884")
                .build();

        given(userDetailsService.getUserDetails(validId)).willReturn(userDetails);

        mockMvc.perform(get("/user/{id}", validId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(userDetails.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDetails.getLastName())))
                .andExpect(jsonPath("$.phoneNumber", is(userDetails.getPhoneNumber())));
    }

    @Test
    public void givenValidImageAndUserName_whenUploadProfileImage_thenReturnsHttpStatusOk() throws Exception {
        byte[] imageBytes = "sample image bytes".getBytes();

        doNothing().when(imageService).saveProfileImage(any(byte[].class), any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/uploadProfileImage/testUser")
                        .content(imageBytes)
                        .contentType(MediaType.IMAGE_JPEG))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenIOException_whenUploadProfileImage_thenReturnsHttpStatusInternalServerError() throws Exception {
        byte[] imageBytes = "sample image bytes".getBytes();

        doThrow(new IOException()).when(imageService).saveProfileImage(any(byte[].class),any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/uploadProfileImage/testUser")
                        .content(imageBytes)
                        .contentType(MediaType.IMAGE_JPEG))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @DisplayName("Given a valid username, when downloadProfileImage is called, then return HttpStatus.OK")
    public void givenValidUsername_whenDownloadProfileImage_thenReturnsHttpStatusOk() throws Exception {
        byte[] imageBytes = "sample image bytes".getBytes();
        final String USER_PROFILE_DOWNLOAD_URL = "/downloadProfileImage/testUser";
        when(imageService.downloadProfileImage(any(String.class))).thenReturn(imageBytes);

        mockMvc.perform(MockMvcRequestBuilders.get(USER_PROFILE_DOWNLOAD_URL)
                .content(imageBytes)
                .contentType(MediaType.IMAGE_JPEG)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().bytes(imageBytes));
    }

    @Test
    @DisplayName("Given an IOException, when downloadProfileImage is called, then return HttpStatus.INTERNAL_SERVER_ERROR")
    public void givenIOException_whenDownloadProfileImage_thenReturnsHttpStatusInternalServerError() throws Exception {
        final String USER_PROFILE_DOWNLOAD_URL = "/downloadProfileImage/testUser";
        when(imageService.downloadProfileImage(any(String.class))).thenThrow(IOException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(USER_PROFILE_DOWNLOAD_URL))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @DisplayName("Given an NotFoundException, when downloadProfileImage is called, then return HttpStatus.NOT_FOUND")
    public void givenNotFoundException_whenDownloadProfileImage_thenReturnsNotFoundException() throws Exception {
        final String USER_PROFILE_DOWNLOAD_URL = "/downloadProfileImage/testUser";
        when(imageService.downloadProfileImage(any(String.class))).thenThrow(NotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(USER_PROFILE_DOWNLOAD_URL))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Given a NotFoundException, when downloadProfileImage is called, then return HttpStatus.NOT_FOUND")
    public void givenNotFoundException_whenDownloadProfileImage_thenReturnsHttpStatusNotFound() throws Exception {
        final String USER_PROFILE_DOWNLOAD_URL = "/downloadProfileImage";
        when(imageService.downloadProfileImage(any(String.class))).thenThrow(new NullPointerException());

        mockMvc.perform(MockMvcRequestBuilders.get(USER_PROFILE_DOWNLOAD_URL))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
