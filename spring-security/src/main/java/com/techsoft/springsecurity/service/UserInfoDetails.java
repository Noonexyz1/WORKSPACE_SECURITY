package com.techsoft.springsecurity.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.techsoft.springsecurity.entity.User;

//Necesitan que este sea un Bean, UserDetails ya esta implementado
public class UserInfoDetails implements UserDetails {

    String userName;
    String password;
    List<GrantedAuthority> authorities;

    //Mi constructor
    public UserInfoDetails(User userInfo){
       userName = userInfo.getName();
       password = userInfo.getPassword();
       authorities = Arrays.stream(userInfo.getRoles().split(","))
               .map(SimpleGrantedAuthority::new)
               .collect(Collectors.toList());
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
