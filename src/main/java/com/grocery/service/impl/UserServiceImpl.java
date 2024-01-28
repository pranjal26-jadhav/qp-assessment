package com.grocery.service.impl;

import com.grocery.dtos.UserDTO;
import com.grocery.entitites.Role;
import com.grocery.entitites.UserEntity;
import com.grocery.exceptionHandler.HttpException;
import com.grocery.repos.UserRepository;
import com.grocery.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  findUserByUsername(username)
                .map(user -> new User(user.getUserName(), user.getPassword(), getAuthority(user)))
                .orElseThrow(() -> new UsernameNotFoundException("Invalid userName and password"));
    }
    private Set<SimpleGrantedAuthority> getAuthority(UserEntity userEntity) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        Optional.ofNullable(userEntity)
                .map(UserEntity::getRole)
                .map(Enum::name)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .ifPresent(authorities::add);
        return authorities;
    }

    @Override
    public void createUser(UserDTO userDTO) {
        try {
            UserEntity userEntity = new UserEntity();

            userEntity.setUserName(Optional.ofNullable(userDTO)
                    .map(UserDTO::getUserName)
                    .filter(StringUtils::isNotBlank)
                    .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "username is missing")));

            userEntity.setName(Optional.ofNullable(userDTO)
                    .map(UserDTO::getName)
                    .filter(StringUtils::isNotBlank)
                    .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "name is missing")));

            userEntity.setPassword(Optional.ofNullable(userDTO)
                    .map(UserDTO::getUserName)
                    .filter(StringUtils::isNotBlank) //TODO: Strong password check
                            .map(password -> passwordEncoder.encode(password))
                    .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "password is missing")));

            userEntity.setRole(Optional.ofNullable(userDTO)
                    .map(UserDTO::getRole)
                    .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "Role is missing")));

            userRepository.save(userEntity);
        } catch (Exception e) {
            log.error("Error in creating user: {}", userDTO.getUserName(), e);
            throw e;
        }
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }


}
