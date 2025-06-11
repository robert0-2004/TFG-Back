package com.proyectofin.proyecto_final.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyectofin.proyecto_final.entities.Terreno;
import com.proyectofin.proyecto_final.repositories.TerrenoRepository;

@Service
public class TerrenoServiceImpl implements TerrenoServiceInter {

    @Autowired
    private TerrenoRepository repository;

    public TerrenoServiceImpl(TerrenoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Terreno> findByUserId(Long userID, Pageable pageable) {
        return repository.findByUserId(userID, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Terreno> findAll() {
        return (List<Terreno>) this.repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Terreno> findById(@NonNull Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public Terreno save(Terreno terreno) {
        return repository.save(terreno);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

      @Override
    @Transactional(readOnly = true)
    public Page<Terreno> searchTerreno(String term, Pageable pageable) {
        return repository.searchByTerm(term, pageable);
    }

    @Override
@Transactional(readOnly = true)
public Page<Terreno> searchTerrenoByUserId(Long userId, String term, Pageable pageable) {
    if (term == null || term.isEmpty()) {
        return repository.findByUserId(userId, pageable);
    }
    return repository.searchByUserIdAndTerm(
        userId, term, pageable
    );
}
}
