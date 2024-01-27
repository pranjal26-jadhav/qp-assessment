package com.grocery.service.impl;

import com.grocery.entitites.UserEntity;
import com.grocery.repos.UserRepository;
import com.grocery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

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
    public void createUser() {
        //TODO: implemented method
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }


}
