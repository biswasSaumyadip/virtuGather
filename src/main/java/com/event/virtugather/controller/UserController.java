package com.event.virtugather.controller;

import com.event.virtugather.model.UserDetails;
import com.event.virtugather.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserDetailsService userDetailsService;

    @GetMapping("/user/{id}")
    public UserDetails getUserDetails(@PathVariable long id) {
        return userDetailsService.getUserDetails(id);
    }


}
