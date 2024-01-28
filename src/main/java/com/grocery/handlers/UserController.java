package com.grocery.handlers;

import com.grocery.dtos.UserDTO;
import com.grocery.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/user")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        log.info("Creating user with username: {}", userDTO.getUserName());
        userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
