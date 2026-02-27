package com.dmq.picoyplaca.infrastructure.adapter.output.persistence;

import com.dmq.picoyplaca.domain.port.output.FeriadoRepositoryPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Adaptador de salida que implementa FeriadoRepositoryPort usando Spring Data
 * JPA.
 * Conecta el dominio con la tabla feriados en base de datos.
 */
@Component
public class FeriadoRepositoryAdapter implements FeriadoRepositoryPort {

    private final FeriadoJpaRepository jpaRepository;

    public FeriadoRepositoryAdapter(FeriadoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * Resultado cacheado por 24h (Caffeine).
     */
    @Override
    @Cacheable(value = "feriados", key = "#fecha")
    public boolean esFeriado(LocalDate fecha) {
        return jpaRepository.existsByFechaAndActivoTrue(fecha);
    }
}
