package com.irojas.demojwt.AuthDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    String username;
    String password;
    String firstname;
    String lastname;
    String country; 
}
