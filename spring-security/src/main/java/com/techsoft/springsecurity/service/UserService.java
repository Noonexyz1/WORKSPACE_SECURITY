package com.techsoft.springsecurity.service;

import com.techsoft.springsecurity.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;    //para encriptar

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.techsoft.springsecurity.entity.User;
import com.techsoft.springsecurity.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUser() {
         return userInfoRepository.findAll();
    }
    public User getUser(Integer id) {
        return userInfoRepository.findById(id).get();
    }

    //Para guardar un nuevo usuario y contraseña en la BD
    public String addUser(UserDTO userDTO) {
        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .roles(userDTO.getRoles())
                .build();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userInfoRepository.save(user);
        return "User added successfully";
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo = userInfoRepository.findByName(username);
        return userInfo.map(element -> new UserInfoDetails(element))
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }
    //si quieres hacerlo por id, pues simplemente haces otro metodo loadUserByUserId(String id)
    //asi tendrias dos metodos

}
