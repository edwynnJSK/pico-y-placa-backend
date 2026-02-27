package com.dmq.picoyplaca.infrastructure.exception;

import java.time.LocalDateTime;

/**
 * DTO de respuesta de error estandarizado para todos los errores de la API.
 *
 * @param codigo    código de error (ej. PLACA_INVALIDA)
 * @param mensaje   descripción del error
 * @param timestamp instante en que ocurrió el error
 */
public record ErrorResponse(
                String codigo,
                String mensaje,
                LocalDateTime timestamp) {
}
