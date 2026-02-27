package com.dmq.picoyplaca.infrastructure.adapter.output.persistence;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad JPA para la tabla reglas_restriccion.
 */
@Entity
@Table(name = "reglas_restriccion")
public class ReglaRestriccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del día de la semana */
    @Column(name = "dia_semana", nullable = false, length = 20)
    private String diaSemana;

    /** CSV de dígitos restringidos, ej. "1,2" o "9,0". */
    @Column(nullable = false, length = 10)
    private String digitos;

    @Column(name = "franja_inicio_1", nullable = false)
    private LocalTime franjaInicio1;

    @Column(name = "franja_fin_1", nullable = false)
    private LocalTime franjaFin1;

    @Column(name = "franja_inicio_2", nullable = false)
    private LocalTime franjaInicio2;

    @Column(name = "franja_fin_2", nullable = false)
    private LocalTime franjaFin2;

    @Column(name = "vigente_desde", nullable = false)
    private LocalDate vigenciaDesde;

    @Column(name = "vigente_hasta")
    private LocalDate vigenciaHasta;

    @Column(nullable = false)
    private boolean activo = true;

    protected ReglaRestriccionEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getDigitos() {
        return digitos;
    }

    public void setDigitos(String digitos) {
        this.digitos = digitos;
    }

    public LocalTime getFranjaInicio1() {
        return franjaInicio1;
    }

    public void setFranjaInicio1(LocalTime franjaInicio1) {
        this.franjaInicio1 = franjaInicio1;
    }

    public LocalTime getFranjaFin1() {
        return franjaFin1;
    }

    public void setFranjaFin1(LocalTime franjaFin1) {
        this.franjaFin1 = franjaFin1;
    }

    public LocalTime getFranjaInicio2() {
        return franjaInicio2;
    }

    public void setFranjaInicio2(LocalTime franjaInicio2) {
        this.franjaInicio2 = franjaInicio2;
    }

    public LocalTime getFranjaFin2() {
        return franjaFin2;
    }

    public void setFranjaFin2(LocalTime franjaFin2) {
        this.franjaFin2 = franjaFin2;
    }

    public LocalDate getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(LocalDate vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public LocalDate getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(LocalDate vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
