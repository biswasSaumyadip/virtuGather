package com.event.virtugather.service.impl;

import com.event.virtugather.constants.UserField;
import com.event.virtugather.dao.UserDao;
import com.event.virtugather.model.User;
import com.event.virtugather.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;


    @Override
    public int createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        int userId = userDao.createUser(user);
        LOGGER.info("User created with ID: {}", userId);
        return userId;
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userDao.isUsernameExist(username);
    }

    @Override
    public String updatedUserField(Long id, UserField field, String newValue) {
        int status = userDao.updateUserField(id, field, newValue);
        if (status == 0) {
            throw new IllegalArgumentException("Invalid user ID or field");
        } else {
            return "User field updated successfully";
        }
    }
}
