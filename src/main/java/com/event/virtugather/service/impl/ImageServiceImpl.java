package com.event.virtugather.service.impl;

import com.event.virtugather.dao.UserDetailsDao;
import com.event.virtugather.exceptions.NotFoundException;
import com.event.virtugather.model.UserDetails;
import com.event.virtugather.service.ImageService;
import com.event.virtugather.utils.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final String UPLOAD_DIRECTORY = "profile_picture";
    private final UserDetailsDao userDetailsDao;

    @Override
    public void saveProfileImage(byte[] imageBytes, String userName) throws IOException {
        String creationDate = LocalDate.now().toString();

        String cleanUserName = validateAndSanitizeUsername(userName);

        String fileName = generateFileName(cleanUserName, creationDate);

        saveImageAndValidate(imageBytes, fileName);

        UserDetails userDetails = userDetailsDao.findByUsername(cleanUserName);
        if(userDetails == null) {
            throw new IllegalArgumentException("User not found: " + cleanUserName);
        }

        userDetailsDao.saveProfileImage(cleanUserName, fileName);
    }

    @Override
    public byte[] downloadProfileImage(String userName) throws IOException {
        UserDetails userDetails = userDetailsDao.findByUsername(userName);

        if (userDetails == null) {
            throw new NotFoundException("User not found: " + userName);
        }

        String profileImageUrl = userDetails.getImageUrl();
        try (InputStream in = new URL(profileImageUrl).openStream()) {
            return IOUtils.toByteArray(in);
        }
    }

    private String validateAndSanitizeUsername(String userName) {
        if(userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Invalid username.");
        }
        return userName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    private String generateFileName(String cleanUserName, String creationDate) {
        return cleanUserName + "_" + creationDate + ".jpg";
    }

    private void saveImageAndValidate(byte[] imageBytes, String fileName) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        if (image == null) {
            throw new IllegalArgumentException("Invalid image file.");
        }
        Path path = Paths.get(UPLOAD_DIRECTORY, fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, imageBytes);
    }
}