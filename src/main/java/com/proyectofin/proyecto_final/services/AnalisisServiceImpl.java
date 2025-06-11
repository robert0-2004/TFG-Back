package com.proyectofin.proyecto_final.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyectofin.proyecto_final.entities.Analisis;
import com.proyectofin.proyecto_final.repositories.AnalisisRepository;
import java.util.List;

@Service
public class AnalisisServiceImpl implements AnalisisService {

    @Autowired
    private AnalisisRepository repository;

    @Override
    public Analisis save(Analisis analisis) {
        return repository.save(analisis);
    }

    @Override
    public List<Analisis> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}