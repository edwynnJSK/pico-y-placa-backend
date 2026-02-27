package com.dmq.picoyplaca.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad FeriadoEntity.
 */
public interface FeriadoJpaRepository extends JpaRepository<FeriadoEntity, Long> {

    /**
     * Verifica si existe un feriado activo en la fecha dada.
     */
    boolean existsByFechaAndActivoTrue(LocalDate fecha);

    /**
     * Lista todos los feriados activos de un año específico.
     */
    @Query("SELECT f FROM FeriadoEntity f WHERE YEAR(f.fecha) = :anio AND f.activo = true ORDER BY f.fecha")
    List<FeriadoEntity> findByAnioAndActivoTrue(@Param("anio") int anio);
}
