package com.event.virtugather.controller;

import com.event.virtugather.DTO.UserDTO;
import com.event.virtugather.model.User;
import com.event.virtugather.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final UserService userService;

    private static final String USER_REG_MSG = "User registered with ID: ";

    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO) {
        log.info("Attempt to register user");
        User user = userDTO.toUser();
        user.securePassword(userDTO.getPassword());
        int userId = userService.createUser(user);
        log.info(USER_REG_MSG + "{}", userId);
        return USER_REG_MSG + userId;
    }

    @GetMapping("/checkUsername/{username}")
    public boolean checkUsername(@PathVariable String username) {
        log.info("Checking if username exists: {}", username);
        return userService.isUsernameExist(username);
    }
}
