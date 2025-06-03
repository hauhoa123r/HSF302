package com.web.service.impl;

import com.web.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("hungngunhucho")
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepositoryImpl;

    @Test
    void isLogin() {
        String username = "nguyenvana";
        String password = "pass123";
        Optional.ofNullable(userRepositoryImpl.findByUsernameAndPassword(username, password))
                .orElseThrow(() -> new RuntimeException("U U Kec Kec"));
    }
}