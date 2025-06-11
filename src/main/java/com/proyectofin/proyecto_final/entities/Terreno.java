package com.proyectofin.proyecto_final.entities;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "terreno")
public class Terreno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private Double superficie;

    @NotBlank
    private String cultivo;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Clave foránea en la tabla terreno
    private User user;


    public Terreno() {}

    public Terreno(long id, @NotNull Double superficie, @NotBlank String cultivo, User user, List<Analisis> analisis) {
        this.id = id;
        this.superficie = superficie;
        this.cultivo = cultivo;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Double superficie) {
        this.superficie = superficie;
    }

    public String getCultivo() {
        return cultivo;
    }

    public void setCultivo(String cultivo) {
        this.cultivo = cultivo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
  
}