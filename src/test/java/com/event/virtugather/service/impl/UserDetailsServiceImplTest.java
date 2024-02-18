package com.event.virtugather.service.impl;

import com.event.virtugather.dao.UserDetailsDao;
import com.event.virtugather.exceptions.NotFoundException;
import com.event.virtugather.exceptions.UserDetailsSaveException;
import com.event.virtugather.model.UserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    UserDetailsDao userDao;

    @InjectMocks
    UserDetailsServiceImpl userDetailsServiceImpl;


    @Test
    @DisplayName("Test for create user details should success and return one")
    void createUserDetails_ShouldReturnOne (){
        UserDetails userDetails = UserDetails.builder()
                .userId(199L)
                .detailId(198L)
                .firstName("blitz")
                .lastName("lilith")
                .phoneNumber("9905892884")
                .build();
        when(userDetailsServiceImpl.createUserDetails(userDetails)).thenReturn(1);

        int result = userDetailsServiceImpl.createUserDetails(userDetails);

        assertTrue(result > 0, "Expected result to be 1");
        verify(userDao).saveUserDetails(userDetails);

    }

    @Test
    void createUserDetails_ShouldHandleNullGracefully() {
        when(userDao.saveUserDetails(null)).thenThrow(UserDetailsSaveException.class);
        assertThrows(UserDetailsSaveException.class, () -> userDetailsServiceImpl.createUserDetails(null));
    }

    @Test
    @DisplayName("Test for update user details should success and return one")
    void updateUserDetails_ShouldReturnOne (){
        UserDetails userDetails = UserDetails.builder()
                .userId(199L)
                .detailId(198L)
                .firstName("blitz")
                .lastName("lilith")
                .phoneNumber("9905892884")
                .build();
        when(userDao.updateUserDetails(userDetails)).thenReturn(1);

        int result = userDetailsServiceImpl.updateUserDetails(userDetails);

        assertTrue(result > 0, "Expected result to be 1");
        verify(userDao).updateUserDetails(userDetails);

    }

    @Test
    void updateUserDetails_ShouldHandleNullGracefully() {
        when(userDao.updateUserDetails(null)).thenThrow(UserDetailsSaveException.class);
        assertThrows(UserDetailsSaveException.class, () -> userDetailsServiceImpl.updateUserDetails(null));
    }

    @Test
    void getUserDetails_ShouldReturnUserDetails() {
        long id = 199L;

        UserDetails userDetails = UserDetails.builder()
                .userId(199L)
                .detailId(198L)
                .firstName("blitz")
                .lastName("lilith")
                .phoneNumber("9905892884")
                .build();

        when(userDao.getUserDetails(id)).thenReturn(userDetails);

        UserDetails result = userDetailsServiceImpl.getUserDetails(id);

        assertEquals(userDetails, result);
        verify(userDao).getUserDetails(id);
    }


    @Test
    void getUserDetails_ShouldHandleNotFoundException() {
        when(userDao.getUserDetails(199L)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () ->userDetailsServiceImpl.getUserDetails(199L));
    }

}

