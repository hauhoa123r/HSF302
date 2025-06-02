package com.web.service.impl;

import com.web.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepositoryImpl;

    @Test
    void isLogin() {
        String username = "johndoe";
        String password = "123456";
        Optional.ofNullable(userRepositoryImpl.findByUsernameAndPassword(username, password))
                .orElseThrow(() -> new RuntimeException("U U Kec Kec"));
    }
}