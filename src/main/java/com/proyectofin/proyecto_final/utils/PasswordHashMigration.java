package com.proyectofin.proyecto_final.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import com.proyectofin.proyecto_final.repositories.UserRepository;
import com.proyectofin.proyecto_final.entities.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Component
public class PasswordHashMigration {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void hashExistingPasswords() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String rawPassword = user.getPassword();
            if (!rawPassword.startsWith("$2a$")) { // Solo si no está ya hasheada
                user.setPassword(passwordEncoder.encode(rawPassword));
            }
        }
        userRepository.saveAll(users);
    }
}