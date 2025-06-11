package com.proyectofin.proyecto_final.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectofin.proyecto_final.entities.Terreno;

public interface TerrenoRepository extends JpaRepository<Terreno, Long> {

    Page<Terreno> findByUserId(Long userId, Pageable pageable);

    List<Terreno> findByUserId(Long userId);

    @Query("SELECT t FROM Terreno t WHERE " +
            "(CAST(t.superficie AS string) LIKE LOWER(CONCAT('%', :term, '%'))) OR " +
            "LOWER(t.cultivo) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Terreno> searchByTerm(@Param("term") String term, Pageable pageable);


@Query("SELECT t FROM Terreno t WHERE t.user.id = :userId AND (" +
       "LOWER(t.cultivo) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
       "CAST(t.superficie AS string) LIKE LOWER(CONCAT('%', :term, '%'))" +
       ")")
Page<Terreno> searchByUserIdAndTerm(@Param("userId") Long userId, @Param("term") String term, Pageable pageable);

}
