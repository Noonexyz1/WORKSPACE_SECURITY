package com.irojas.demojwt.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.irojas.demojwt.JwtUtils.JwtUtilsAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {   
    //Es necesario configurar esta clase porque Spring la protege por defecto todos los endPoints
    //Estos seran mis Filtros
    //Ponemos los EndPoints que sera publicos y los que seran privados

    //Estos estan siendo injectados
    private final JwtUtilsAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //retornmaos el http siempre y cuando pase por una serie de filtros
        return http
            .csrf(csrf -> csrf.disable())   //lo quitamos por que spring lo pone por defecto, pero como son apis staticas, no lo necesitamos
            .authorizeHttpRequests(authRequest -> authRequest.requestMatchers("/auth/**")
                                                             .permitAll()       //si aquellos macheen con "/auth/*"  entonces le damos acceso
                                                             .anyRequest()
                                                             .authenticated() )  //pero cualquier otro le voy a pedir que se autentique
            
            .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

}
