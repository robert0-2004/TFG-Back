package com.proyectofin.proyecto_final.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.proyectofin.proyecto_final.entities.Terreno;
import com.proyectofin.proyecto_final.repositories.TerrenoRepository;

@Service
public class TerrenoService {

    @Autowired
    private TerrenoRepository terrenoRepository;

    public List<Terreno> getTerrenosByUser(Long userId) {
        return terrenoRepository.findByUserId(userId);
    }

    public Page<Terreno> findByUserId(Long userId, Pageable pageable) {
        return terrenoRepository.findByUserId(userId, pageable);
    }
}
