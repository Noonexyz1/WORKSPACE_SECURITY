package com.irojas.demojwt.Demo.api.publico;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.irojas.demojwt.AuthDTOs.AuthResponseDTO;
import com.irojas.demojwt.AuthDTOs.LoginRequestDTO;
import com.irojas.demojwt.AuthDTOs.RegisterRequestDTO;
import com.irojas.demojwt.JwtUtils.JwtUtilsService;
import com.irojas.demojwt.Models.Role;
import com.irojas.demojwt.Models.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository userRepository;
    private final JwtUtilsService jwtUtilsService;

    //estas clases esta siendo injectado
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtUtilsService.getToken(user);
        return AuthResponseDTO.builder()
            .token(token)
            .build();
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))//encripta el pass del usuario para la base de datos
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .country(request.getCountry())
            .role(Role.USER)
            .build();

        userRepository.save(user);

        return AuthResponseDTO.builder()
            .token(jwtUtilsService.getToken(user))
            .build();
    }

}
