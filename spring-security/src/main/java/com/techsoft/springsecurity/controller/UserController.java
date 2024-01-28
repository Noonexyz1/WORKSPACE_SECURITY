package com.techsoft.springsecurity.controller;

import com.techsoft.springsecurity.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.techsoft.springsecurity.dtos.AuthRequestDTO;
import com.techsoft.springsecurity.entity.User;
import com.techsoft.springsecurity.service.JwtService;
import com.techsoft.springsecurity.service.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userInfoService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Spring Security tutorials !!";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestBody UserDTO userDTO) {
        return userInfoService.addUser(userDTO);
    }


    @PostMapping("/login")  //Le esta devolviendo el jwToken
    public String addUser(@RequestBody AuthRequestDTO authRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;
        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authRequestDTO.getUserName(), authRequestDTO.getPassword());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(authRequestDTO.getUserName());
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('ADMIN_ROLES')")
    //asegura que el método sólo pueda ser invocado por usuarios que tengan la autoridad ‘USER_ROLES’.
    public List<User> getAllUsers() {
        return userInfoService.getAllUser();
    }

    @GetMapping("/getUsers/{id}")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public User getAllUsers(@PathVariable Integer id) {
        return userInfoService.getUser(id);
    }

}
