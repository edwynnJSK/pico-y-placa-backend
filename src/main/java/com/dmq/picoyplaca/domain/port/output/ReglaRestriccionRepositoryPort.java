package com.dmq.picoyplaca.domain.port.output;

import com.dmq.picoyplaca.domain.model.ReglaRestriccion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Puerto de salida para consulta de reglas de restricción vehicular.
 * Permite que el dominio acceda a las reglas almacenadas en base de datos
 */
public interface ReglaRestriccionRepositoryPort {

    /**
     * @param diaSemana día a consultar (ej. MONDAY, TUESDAY)
     * @param fecha     fecha para filtrar por vigencia de la regla
     * @return Optional con la regla vigente, o vacío si no existe
     */
    Optional<ReglaRestriccion> buscarPorDiaYFecha(DayOfWeek diaSemana, LocalDate fecha);
}
