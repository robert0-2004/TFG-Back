package com.proyectofin.proyecto_final.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.proyectofin.proyecto_final.entities.Terreno;

public interface TerrenoServiceInter {

    List<Terreno> findAll();

    Page<Terreno> findByUserId(Long userID, Pageable pageable);

    Optional<Terreno> findById(@NonNull Long id);

    Terreno save(Terreno terreno);

    void deleteById(Long id);

    Page<Terreno> searchTerreno(String term, Pageable pageable);

   Page<Terreno> searchTerrenoByUserId(Long userId, String term, Pageable pageable);

}