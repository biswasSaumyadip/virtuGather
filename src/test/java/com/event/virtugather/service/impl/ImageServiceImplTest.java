package com.event.virtugather.service.impl;

import com.event.virtugather.dao.UserDetailsDao;
import com.event.virtugather.exceptions.NotFoundException;
import com.event.virtugather.model.UserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testing ImageServiceImpl")
public class ImageServiceImplTest {
    private static final String UPLOAD_DIRECTORY = "profile_picture";
    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private UserDetailsDao userDetailsDao;

    @Test
    @DisplayName("Test profile image successfully saved")
    public void testSaveProfileImage_Successful() throws IOException {

        String userName = "User1";
        String expectedFileName = userName + "_" + LocalDate.now() + ".jpg";

        // Path to the test image file
        Path imagePath = Paths.get("src", "test", "resources", "test_image.jpg");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        UserDetails userDetails = new UserDetails();
        when(userDetailsDao.findByUsername(userName)).thenReturn(userDetails);

        // WHEN
        imageService.saveProfileImage(imageBytes, userName);

        // THEN
        assertTrue(Files.exists(Path.of(UPLOAD_DIRECTORY, expectedFileName)));
    }

    @Test
    @DisplayName("Test save profile image with user not found")
    public void testSaveProfileImage_UserNotFound() throws IOException {
        // GIVEN
        String userName = "User2";
        Path imagePath = Paths.get("src", "test", "resources", "test_image.jpg");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        when(userDetailsDao.findByUsername(userName)).thenReturn(null);

        // WHEN
        assertThrows(IllegalArgumentException.class,() -> {
            imageService.saveProfileImage(imageBytes, userName);
        // THEN
        });

    }

    @Test
    @DisplayName("Test save profile image with invalid image bytes")
    public void testSaveProfileImage_InvalidImageBytes() {
        // GIVEN
        byte[] imageBytes = new byte[0]; 
        String userName = "User3";

        // WHEN
        assertThrows(IllegalArgumentException.class,() ->{
            imageService.saveProfileImage(imageBytes, userName);
        // THEN
        });

    }

    @Test
    @DisplayName("When User Exists, then return downloaded profile image")
    public void testDownloadProfileImage_Success() throws Exception {
        // given
        String testUserName = "testUser";
        String testUrl = "http://localhost/test.jpg";

        UserDetails testUserDetails = new UserDetails();
        testUserDetails.setImageUrl(testUrl);

        when(userDetailsDao.findByUsername(testUserName)).thenReturn(testUserDetails);

        URL mockUrl = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments(testUrl).thenReturn(mockUrl);

        byte[] expectedOutput = "Image data".getBytes();
        InputStream mockStream = new ByteArrayInputStream(expectedOutput);

        when(mockUrl.openStream()).thenReturn(mockStream);

        // when
        byte[] result = imageService.downloadProfileImage(testUserName);

        // then
        assertNotNull(result);
        assertEquals(expectedOutput.length, result.length);
        assertTrue(Arrays.equals(expectedOutput, result));

        verify(userDetailsDao, times(1)).findByUsername(testUserName);
    }

    @Test
    @DisplayName("When User Not Found, then throw NotFoundException")
    public void testDownloadProfileImage_FailWhenUserNotFound() {

        String testUserName = "testUser";

        when(userDetailsDao.findByUsername(testUserName)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> imageService.downloadProfileImage(testUserName));

        verify(userDetailsDao, times(1)).findByUsername(testUserName);
    }

    @Test
    @DisplayName("When Malformed URL, then throw MalformedURLException")
    public void testDownloadProfileImage_FailWhenMalformedURLException() {

        String testUserName = "testUser";
        String testUrl = "malformed/url";

        UserDetails testUserDetails = new UserDetails();
        testUserDetails.setImageUrl(testUrl);

        when(userDetailsDao.findByUsername(testUserName)).thenReturn(testUserDetails);

        assertThrows(MalformedURLException.class, () -> imageService.downloadProfileImage(testUserName));

        verify(userDetailsDao, times(1)).findByUsername(testUserName);
    }

}