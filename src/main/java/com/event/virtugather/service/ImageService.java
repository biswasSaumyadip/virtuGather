package com.event.virtugather.service;

import java.io.IOException;

public interface ImageService {
    void saveProfileImage(byte[] imageBytes, String userName) throws IOException;
    byte[] downloadProfileImage(String userName) throws IOException;
}
