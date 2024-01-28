package com.irojas.demojwt.Demo.api.publico;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irojas.demojwt.Models.User;

public interface AuthRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username); 
}
