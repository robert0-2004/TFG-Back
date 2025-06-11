package com.proyectofin.proyecto_final.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proyectofin.proyecto_final.entities.Role;
import com.proyectofin.proyecto_final.entities.User;

import io.micrometer.common.lang.NonNull;

public interface UserServiceInt {

    Page<User> findAll(Pageable pageable);

    Page<User> findByRol(List<Role> roles, Pageable pageable);

    Optional<User> findById(@NonNull Long id);

    User save(User user);

    void deleteById(Long id);

    Page<User> searchUsers(String term, Pageable pageable);

}
