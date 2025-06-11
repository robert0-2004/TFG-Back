package com.proyectofin.proyecto_final.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectofin.proyecto_final.entities.Analisis;

public interface AnalisisRepository extends JpaRepository<Analisis, Long>  {
     List<Analisis> findByUserId(Long userId);

}
