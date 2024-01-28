package com.grocery.service;

import com.grocery.dtos.UserDTO;
import com.grocery.entitites.UserEntity;

import java.util.Optional;

public interface UserService {
    void createUser(UserDTO userDTO);
    Optional<UserEntity> findUserByUsername(String userName);
}
