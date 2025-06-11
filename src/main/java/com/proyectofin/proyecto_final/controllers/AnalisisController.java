package com.proyectofin.proyecto_final.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.proyectofin.proyecto_final.entities.Analisis;
import com.proyectofin.proyecto_final.entities.User;
import com.proyectofin.proyecto_final.repositories.AnalisisRepository;
import com.proyectofin.proyecto_final.repositories.UserRepository;
import com.proyectofin.proyecto_final.services.AnalisisService;
import java.util.List;

@RestController
@RequestMapping("/analisis")
public class AnalisisController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnalisisRepository analisisRepository;

    @Autowired
    private AnalisisService analisisService;

    @PostMapping
    public Analisis create(@RequestBody Analisis analisis) {
        Long userId = analisis.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        analisis.setUser(user);
        return analisisRepository.save(analisis);
    }

    @GetMapping("/user/{userId}")
    public List<Analisis> getByUser(@PathVariable Long userId) {
        return analisisService.findByUserId(userId);
    }
}