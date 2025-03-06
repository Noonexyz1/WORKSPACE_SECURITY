package com.security.service;

import com.security.persistence.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataUser {

    private List<UserEntity> listUsuario = new ArrayList<>();

    private PasswordEncoder passwordEncoder; // Inyectar el PasswordEncoder

    public DataUser(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        // Cifrar las contraseñas antes de almacenarlas
        listUsuario.add(UserEntity.builder().username("USUARIO1").password(passwordEncoder.encode("123")).build());
        listUsuario.add(UserEntity.builder().username("USUARIO2").password(passwordEncoder.encode("123")).build());
        listUsuario.add(UserEntity.builder().username("USUARIO3").password(passwordEncoder.encode("123")).build());
    }

    public UserEntity encontrarUsuarioPorCi(String userName) {
        return listUsuario.stream()
                .filter(x -> x.getUsername().equals(userName))
                .findFirst()
                .orElse(null);
    }

    public void guardarNuevoUsuario(UserEntity newUserEntity) {
        // Cifrar la contraseña antes de guardar el usuario
        newUserEntity.setPassword(passwordEncoder.encode(newUserEntity.getPassword()));
        listUsuario.add(newUserEntity);
    }
}
