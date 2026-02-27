package com.dmq.picoyplaca.infrastructure.exception;

import java.time.LocalDateTime;

/**
 * Excepción lanzada cuando la fecha consultada es anterior al momento actual.
 */
public class FechaInvalidaException extends RuntimeException {

    private final LocalDateTime fechaConsultada;

    public FechaInvalidaException(LocalDateTime fechaConsultada) {
        super("La fecha y hora ingresada es anterior al momento actual. "
                + "Solo se permiten consultas presentes o futuras.");
        this.fechaConsultada = fechaConsultada;
    }

    public FechaInvalidaException(LocalDateTime fechaConsultada, Throwable cause) {
        super("La fecha y hora ingresada es anterior al momento actual. "
                + "Solo se permiten consultas presentes o futuras.", cause);
        this.fechaConsultada = fechaConsultada;
    }

    public LocalDateTime getFechaConsultada() {
        return fechaConsultada;
    }
}
