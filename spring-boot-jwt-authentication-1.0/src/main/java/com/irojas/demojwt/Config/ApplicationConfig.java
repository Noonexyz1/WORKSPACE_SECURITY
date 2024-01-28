package com.irojas.demojwt.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.irojas.demojwt.Demo.api.publico.AuthRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    // Inyectas el repositorio de usuarios
    private final AuthRepository userRepository;

    // Creas un bean para el gestor de autenticación. Este gestor es responsable de la autenticación en Spring Security.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Creas un bean para el proveedor de autenticación. Este proveedor utiliza DaoAuthenticationProvider, que es un
    // proveedor de autenticación que utiliza Spring Data para autenticar a los usuarios.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    
    // Creas un bean para el codificador de contraseñas. Este codificador utiliza BCrypt, que es un algoritmo
    // fuerte para la codificación de contraseñas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Creas un bean para el servicio de detalles del usuario. Este servicio carga los detalles del usuario por su
    // nombre de usuario desde el repositorio de usuarios.
    @Bean
    public UserDetailsService userDetailService() {
        return username -> userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

}

