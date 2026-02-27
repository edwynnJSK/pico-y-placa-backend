package com.dmq.picoyplaca.infrastructure.adapter.output.persistence;

import com.dmq.picoyplaca.domain.model.FranjaHoraria;
import com.dmq.picoyplaca.domain.model.ReglaRestriccion;
import com.dmq.picoyplaca.domain.port.output.ReglaRestriccionRepositoryPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de salida que implementa ReglaRestriccionRepositoryPort
 * usando Spring Data JPA. Convierte ReglaRestriccionEntity al modelo de
 * dominio.
 */
@Component
public class ReglaRestriccionRepositoryAdapter implements ReglaRestriccionRepositoryPort {

    private final ReglaRestriccionJpaRepository jpaRepository;

    public ReglaRestriccionRepositoryAdapter(ReglaRestriccionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * Resultado cacheado para minimizar consultas repetidas a BD.
     */
    @Override
    @Cacheable(value = "reglas", key = "#diaSemana.name() + '_' + #fecha")
    public Optional<ReglaRestriccion> buscarPorDiaYFecha(DayOfWeek diaSemana, LocalDate fecha) {
        List<ReglaRestriccionEntity> entidades = jpaRepository.findVigentesByDiaSemanaAndFecha(diaSemana.name(), fecha);

        return entidades.stream()
                .findFirst()
                .map(this::convertirADominio);
    }

    private ReglaRestriccion convertirADominio(ReglaRestriccionEntity entidad) {
        FranjaHoraria franjaManana = new FranjaHoraria(
                entidad.getFranjaInicio1(),
                entidad.getFranjaFin1());
        FranjaHoraria franjaVespertina = new FranjaHoraria(
                entidad.getFranjaInicio2(),
                entidad.getFranjaFin2());

        return new ReglaRestriccion(
                entidad.getId(),
                DayOfWeek.valueOf(entidad.getDiaSemana()),
                ReglaRestriccion.parsearDigitos(entidad.getDigitos()),
                franjaManana,
                franjaVespertina,
                entidad.getVigenciaDesde(),
                entidad.getVigenciaHasta(),
                entidad.isActivo());
    }
}
