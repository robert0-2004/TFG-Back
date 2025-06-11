package com.proyectofin.proyecto_final.services;

import java.util.List;

import com.proyectofin.proyecto_final.entities.Analisis;

public interface AnalisisService {

    Analisis save(Analisis analisis);
    List<Analisis> findByUserId(Long userId);

}
