package com.proyectofin.proyecto_final.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proyectofin.proyecto_final.entities.Terreno;
import com.proyectofin.proyecto_final.entities.User;
import com.proyectofin.proyecto_final.repositories.UserRepository;
import com.proyectofin.proyecto_final.services.PdfService;
import com.proyectofin.proyecto_final.services.TerrenoService;
import com.proyectofin.proyecto_final.services.TerrenoServiceInter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/terrenos")
public class TerrenoController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TerrenoService service;

    @Autowired
    private TerrenoServiceInter terrenoService;

    @Autowired
    private PdfService pdfService;

    // Método para generar PDF de un terreno
    @GetMapping("/{terrenoId}/pdf")
    public ResponseEntity<byte[]> generatePdf(
            @PathVariable Long terrenoId,
            @RequestParam(defaultValue = "inline") String disposition) {

        Optional<Terreno> terrenoOptional = terrenoService.findById(terrenoId);

        if (!terrenoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Terreno terreno = terrenoOptional.get();
        byte[] pdfBytes = pdfService.generateTerrenoPdf(terreno);

        // Configurar headers para mostrar en navegador o descargar
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        // Si disposition es "attachment", el navegador descargará el PDF
        // Si es "inline", lo mostrará en el navegador
        String filename = "terreno_" + terrenoId + ".pdf";
        headers.setContentDispositionFormData(disposition, filename);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/mis-terrenos/{userId}")
    public ResponseEntity<List<Terreno>> getTerrenosByUser(@PathVariable Long userId) {
        List<Terreno> terrenos = service.getTerrenosByUser(userId);
        return ResponseEntity.ok(terrenos);
    }

@GetMapping("/mis-terrenos/{userId}/page/{page}")
public Page<Terreno> listPageableByUser(
        @PathVariable Long userId,
        @PathVariable Integer page,
        @RequestParam(defaultValue = "4") int size,
        @RequestParam(defaultValue = "") String term) {

    Pageable pageable = PageRequest.of(page, size);

    // Si tienes un método que filtre por usuario y término de búsqueda:
    if (term == null || term.isEmpty()) {
        return terrenoService.findByUserId(userId, pageable);
    }
    // Debes implementar este método en tu servicio/repositorio:
    return terrenoService.searchTerrenoByUserId(userId, term, pageable);
}

    @GetMapping("/{userId}")
    public ResponseEntity<?> show(@PathVariable Long userId) {
        Optional<Terreno> terrenoOptional = terrenoService.findById(userId);
        if (terrenoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(terrenoOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", "el usuario no se encontro por el id:" + userId));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Terreno terreno, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }

        // Verificar si el User con ID proporcionado existe
        if (terreno.getUser() == null || terreno.getUser().getId() == null) {
            return ResponseEntity.badRequest().body("El terreno debe estar asociado a un usuario válido.");
        }

        // Aquí se debe hacer la validación si el usuario existe, de no ser así,
        // retornar error
        Optional<User> userOptional = userRepository.findById(terreno.getUser().getId());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el usuario con el ID: " + terreno.getUser().getId());
        }

        // Asignar el User encontrado al Terreno
        terreno.setUser(userOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(terrenoService.save(terreno));
    }

    @PutMapping("/{terrenoId}")
    public ResponseEntity<?> update(@Valid @RequestBody Terreno terreno, BindingResult result,
            @PathVariable Long terrenoId) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Optional<Terreno> terrenoOptional = terrenoService.findById(terrenoId);

        if (terrenoOptional.isPresent()) {
            Terreno terrenoDb = terrenoOptional.get();
            terrenoDb.setSuperficie(terreno.getSuperficie());
            terrenoDb.setCultivo(terreno.getCultivo());
            return ResponseEntity.ok(terrenoService.save(terrenoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{terrenoId}")
    public ResponseEntity<?> delete(@PathVariable Long terrenoId) {
        Optional<Terreno> terrenoOptional = terrenoService.findById(terrenoId);
        if (terrenoOptional.isPresent()) {
            terrenoService.deleteById(terrenoId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<String, String>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String term) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Terreno> result = terrenoService.searchTerreno(term, pageable);

        return ResponseEntity.ok(result);
    }
}