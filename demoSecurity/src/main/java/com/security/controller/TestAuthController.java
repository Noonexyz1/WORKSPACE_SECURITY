package com.security.controller;

import com.security.persistence.UserEntity;
import com.security.service.DataUser;
import com.security.service.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class TestAuthController {

    @Autowired
    private DataUser dataUser;
    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Faltaba inyectarlo
    @Autowired
    private PasswordEncoder passwordEncoder; // Faltaba inyectarlo


    @GetMapping("/home")
    public String hello(){
        return "PATH PUBLICO";
    }

    @GetMapping("/hello-secured")
    public ResponseEntity<String> helloSecured() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.ok("Usuario autenticado, acceso permitido.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No ha iniciado sesión");
        }
    }


    @PostMapping("/iniciarSesion")
    public ResponseEntity<String> iniciarSesion(HttpServletRequest request, @RequestBody UserEntity userEntity) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEntity.getUsername());

        if (userDetails != null && passwordEncoder.matches(userEntity.getPassword(), userDetails.getPassword())) {
            // Autenticar manualmente
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authToken);

            // Asignar sesión a Spring Security
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            return ResponseEntity.ok("Sesión iniciada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @GetMapping("/cerrarSesion")
    public ResponseEntity<String> cerrarSesion(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 🔥 Cierra la sesión
        }

        SecurityContextHolder.clearContext(); // 🔥 Borra la autenticación en memoria

        // 🔥 Invalida la cookie de sesión JSESSIONID
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ⚠️ Pon `true` si usas HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // 🔥 Expira inmediatamente
        response.addCookie(cookie);

        return ResponseEntity.ok("Sesión cerrada correctamente");
    }


    @PostMapping("/crearUsuario")
    public String crearUsuario(@RequestBody UserEntity userEntity){
        dataUser.guardarNuevoUsuario(userEntity);
        return "Hello World Secured";
    }
}
