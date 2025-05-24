package com.web.repository;

import com.web.entity.UserEntity;
import com.web.model.dto.UserLoginDTO;
import com.web.model.response.UserLoginResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsernameAndPassword(UserLoginDTO userLoginDTO);
}
