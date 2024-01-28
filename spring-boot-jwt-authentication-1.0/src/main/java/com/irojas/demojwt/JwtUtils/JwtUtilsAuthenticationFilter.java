package com.irojas.demojwt.JwtUtils;

import java.io.IOException;

import org.springframework.http.HttpHeaders;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtilsAuthenticationFilter extends OncePerRequestFilter {

    // Este servicio se importa desde el mismo paquete y se utiliza para las operaciones relacionadas con JWT
    private final JwtUtilsService jwtService;

    // Este servicio se inyecta y se utiliza para cargar los detalles del usuario durante la autenticación
    private final UserDetailsService userDetailsService;
    
    
    @Override   // Aquí es donde se realiza la lógica de filtrado y autenticación
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Extraemos el token de la cabecera
        final String token = getTokenFromRequest(request);
        final String username;

        // Si el token es nulo, pasamos la solicitud al siguiente filtro en la cadena
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraemos el nombre de usuario del token
        username = jwtService.getUsernameFromToken(token);

        // Si el nombre de usuario existe pero no se encuentra en el CONTEXTO DE SEGURIDAD, entonces autenticamos al usuario
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Esta parte es importante porque estamos consultando la base de datos para obtener el objeto UserDetails correspondiente al nombre de usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Si el token es válido, entonces autenticamos al usuario y lo incluimos en el CONTEXTO DE SEGURIDAD
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establecemos la autenticación del usuario en el CONTEXTO DE SEGURIDAD
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Si el token no es válido o si el nombre de usuario no existe, se pasa la solicitud al siguiente filtro en la cadena de filtros
        filterChain.doFilter(request, response);
    }



    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }



    
}
