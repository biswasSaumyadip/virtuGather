package com.event.virtugather.controller;

import com.event.virtugather.exceptions.NotFoundException;
import com.event.virtugather.model.UserDetails;
import com.event.virtugather.service.ImageService;
import com.event.virtugather.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsService userDetailsService;
    private final ImageService imageService;


    @GetMapping("/user/{id}")
    public UserDetails getUserDetails(@PathVariable long id) {
        return userDetailsService.getUserDetails(id);
    }

    @PostMapping("/user")
    public int createUserDetails(@RequestBody UserDetails userDetails) {
        return userDetailsService.createUserDetails(userDetails);
    }

    @PostMapping("/user/{id}")
    public int updateUserDetails(@PathVariable long id, @RequestBody UserDetails userDetails) {
        userDetails.setUserId(id);
        return userDetailsService.updateUserDetails(userDetails);
    }

    @PostMapping("/uploadProfileImage/{username}")
    public ResponseEntity<?> uploadProfileImage(@RequestBody byte[] imageBytes, @PathVariable String username) throws IOException {
        imageService.saveProfileImage(imageBytes, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/downloadProfileImage/{username}")
    public ResponseEntity<byte[]> downloadProfileImage(@PathVariable String username) throws IOException {
        byte[] imageBytes = imageService.downloadProfileImage(username);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);

        // Setting Content-Disposition header
        String fileName = URLEncoder.encode(username + ".jpeg", StandardCharsets.UTF_8);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}
