package com.grocery.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grocery.entitites.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class UserDTO {
    private String userName;
    private String password;
    private String name;
    private Role role;
}
