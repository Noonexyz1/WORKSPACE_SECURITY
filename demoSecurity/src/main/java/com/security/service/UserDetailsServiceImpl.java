package com.security.service;

import com.security.persistence.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private DataUser dataUser;

    // La tabla que es candidado a ser un UserDetails es aquella que tiene los campos username y pass o similares
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Buscar el usuario en tu lista de usuarios
        UserEntity userEntity = dataUser.encontrarUsuarioPorCi(username);

        // Si no se encuentra el usuario, lanzar una excepción
        if (userEntity == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        // Convertir tu UserEntity a UserDetails
        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                //.roles("USER") // Puedes omitir los roles si no los necesitas
                .build();
    }
}
