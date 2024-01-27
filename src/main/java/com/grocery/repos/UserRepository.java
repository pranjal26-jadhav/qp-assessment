package com.grocery.repos;

import com.grocery.entitites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    //@Query("SELECT u FROM  UserEntity u where u.userName = ?1")
    Optional<UserEntity> findByUserName(String name);
}
