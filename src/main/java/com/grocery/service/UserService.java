package com.grocery.service;

import com.grocery.entitites.UserEntity;

import java.util.Optional;

public interface UserService {
    void createUser();
    Optional<UserEntity> findUserByUsername(String userName);
}
