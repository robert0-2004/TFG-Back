package com.proyectofin.proyecto_final.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.proyectofin.proyecto_final.auth.JwtGenerate;
import com.proyectofin.proyecto_final.entities.Role;
import com.proyectofin.proyecto_final.entities.User;
import com.proyectofin.proyecto_final.enums.RoleName;
import com.proyectofin.proyecto_final.services.EmailService;
import com.proyectofin.proyecto_final.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtGenerate jwtGenerate;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");

        Optional<User> user = userService.authenticate(email, password);

        if (user.isPresent()) {
            List<RoleName> roles = user.get().getRoles().stream()
                    .map(role -> role.getName())
                    .toList();
            String token = jwtGenerate.generateToken(user.get().getId(), email, roles);
            return ResponseEntity.ok(Map.of("message", "Login successful", "token", token, "user", user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        User savedUser = userService.save(user);

        // Enviar email de registro
        try {
            emailService.sendRegistrationEmail(savedUser);
        } catch (Exception e) {
            // Loguear el error pero no afectar la respuesta al cliente
            System.err.println("Error al enviar email: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        System.out.println("Request for user with ID: " + id);
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<String, String>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getUserByRol(@RequestParam List<Role> role) {
        List<User> users = userService.getUserByRol(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/list/page/{page}")
    public Page<User> listPageableByUser(@RequestParam List<Role> rol, @PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return userService.findByRol(rol, pageable); // Nuevo método en el servicio
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result,
            @PathVariable Long userId) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<User> userOptional = userService.findById(userId);

        if (userOptional.isPresent()) {
            User userDb = userOptional.get();
            userDb.setName(user.getName());
            userDb.setLastname(user.getLastname());
            userDb.setEmail(user.getEmail());
            userDb.setUsername(user.getUsername());
            userDb.setPassword(user.getPassword());
            return ResponseEntity.ok(userService.save(userDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent()) {
            userService.deleteById(userId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String term) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> result = userService.searchUsers(term, pageable);

        return ResponseEntity.ok(result);
    }

}