package com.event.virtugather.service.impl;

import com.event.virtugather.dao.UserDetailsDao;
import com.event.virtugather.model.UserDetails;
import com.event.virtugather.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsDao userDetailsDao;

    @Override
    public int createUserDetails(UserDetails userDetails) {
        return userDetailsDao.saveUserDetails(userDetails);
    }

    @Override
    public int updateUserDetails(UserDetails userDetails) {
        return userDetailsDao.updateUserDetails(userDetails);
    }
}
