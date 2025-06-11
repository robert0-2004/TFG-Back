package com.proyectofin.proyecto_final.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyectofin.proyecto_final.entities.Role;
import com.proyectofin.proyecto_final.entities.User;

import com.proyectofin.proyecto_final.repositories.UserRepository;

@Service
public class UserService implements UserServiceInt {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getUserByRol(List<Role> roles) {
        return repository.findByRolesIn(roles);
    }

    @Transactional(readOnly = true)
    public Page<User> findByRol(List<Role> roles, Pageable pageable) {
        return repository.findByRolesIn(roles, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return (Page<User>) this.repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(@NonNull Long id) {
        return repository.findById(id);
    }

    @Transactional
    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = repository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> searchUsers(String term, Pageable pageable) {
        return repository.searchByTerm(term, pageable);
    }

}