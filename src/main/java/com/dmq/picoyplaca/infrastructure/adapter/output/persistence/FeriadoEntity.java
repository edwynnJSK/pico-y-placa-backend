package com.dmq.picoyplaca.infrastructure.adapter.output.persistence;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla feriados.
 */
@Entity
@Table(name = "feriados", uniqueConstraints = @UniqueConstraint(columnNames = "fecha"))
public class FeriadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate fecha;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void alPersistir() {
        this.createdAt = LocalDateTime.now();
    }

    protected FeriadoEntity() {
    }

    public FeriadoEntity(LocalDate fecha, String nombre, String tipo) {
        this.fecha = fecha;
        this.nombre = nombre;
        this.tipo = tipo;
        this.activo = true;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
