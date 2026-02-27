package com.dmq.picoyplaca.domain.port.output;

import java.time.LocalDate;

/**
 * Puerto de salida para consulta de feriados nacionales.
 * El dominio utiliza este contrato para determinar si una fecha es feriado
 */
public interface FeriadoRepositoryPort {

    /**
     * @param fecha fecha a evaluar
     * @return True si existe un feriado activo en esa fecha
     */
    boolean esFeriado(LocalDate fecha);
}
