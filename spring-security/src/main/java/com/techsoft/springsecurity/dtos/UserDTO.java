package com.techsoft.springsecurity.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserDTO {
    private String name;
    private String email;
    private String roles;
    private String password;
}
