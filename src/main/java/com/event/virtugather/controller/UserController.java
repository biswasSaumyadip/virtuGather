package com.event.virtugather.controller;

import com.event.virtugather.model.UserDetails;
import com.event.virtugather.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsService userDetailsService;

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

}
