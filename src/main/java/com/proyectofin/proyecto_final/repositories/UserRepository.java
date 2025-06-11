package com.proyectofin.proyecto_final.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyectofin.proyecto_final.entities.Role;
import com.proyectofin.proyecto_final.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByRolesIn(List<Role> roles);

    Page<User> findByRolesIn(List<Role> roles, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(u.lastname) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<User> searchByTerm(@Param("term") String term, Pageable pageable);

}