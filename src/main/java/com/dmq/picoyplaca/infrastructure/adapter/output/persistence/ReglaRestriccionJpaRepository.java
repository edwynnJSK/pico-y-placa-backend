package com.dmq.picoyplaca.infrastructure.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio Spring Data JPA para ReglaRestriccionEntity.
 */
public interface ReglaRestriccionJpaRepository extends JpaRepository<ReglaRestriccionEntity, Long> {

        /**
         * Obtiene las reglas activas para un día de la semana que estén vigentes en la
         * fecha dada.
         */
        @Query("""
                        SELECT r FROM ReglaRestriccionEntity r
                        WHERE r.diaSemana = :diaSemana
                          AND r.activo = true
                          AND r.vigenciaDesde <= :fecha
                          AND (r.vigenciaHasta IS NULL OR r.vigenciaHasta >= :fecha)
                        ORDER BY r.vigenciaDesde DESC
                        """)
        List<ReglaRestriccionEntity> findVigentesByDiaSemanaAndFecha(
                        @Param("diaSemana") String diaSemana,
                        @Param("fecha") LocalDate fecha);
}
