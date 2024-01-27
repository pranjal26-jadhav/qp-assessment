package com.grocery.handlers;

import com.grocery.entitites.Role;
import com.grocery.entitites.UserEntity;
import com.grocery.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/demo")
@Slf4j
public class DemoController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/test")
    @Transactional
    public void test()  {
        log.info("/test");
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUserName("PA");
            userEntity.setPassword(passwordEncoder.encode("PA"));
            userEntity.setRole(Role.USER);
            userEntity.setName("PA");
            userRepository.save(userEntity);

            UserEntity adminEnitiy = new UserEntity();
            adminEnitiy.setUserName("pranjal");
            adminEnitiy.setPassword(passwordEncoder.encode("pranjal"));
            adminEnitiy.setRole(Role.ADMIN);
            adminEnitiy.setName("pranjal");

            userRepository.save(adminEnitiy);


        } catch (Exception e) {
            log.error("error", e);
        }
    }
}
