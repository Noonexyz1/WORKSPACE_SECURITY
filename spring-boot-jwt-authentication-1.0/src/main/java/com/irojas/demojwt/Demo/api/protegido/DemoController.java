package com.irojas.demojwt.Demo.api.protegido;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DemoController {   //Aqui iran mis endPoints Protegidos
                                //Cuando agregamos Spring Security, todas las rutas es protegidas por defecto
    
    @PostMapping(value = "demo")
    public String welcome() {
        return "Welcome from secure endpoint";
    }
    
}
