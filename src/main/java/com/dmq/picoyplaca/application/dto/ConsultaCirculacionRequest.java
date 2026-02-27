package com.dmq.picoyplaca.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

/**
 * DTO de entrada para la consulta de restricción vehicular.
 * La validación de formato de placa a nivel Bean Validation como pre-filto;
 *
 * @param placa
 * @param fechaHora
 */
public record ConsultaCirculacionRequest(

                @NotBlank(message = "La placa no puede estar vacía.") @Pattern(regexp = "^[ABCEGHIJKLMNOPQRSTUVWXZ][A-Z]{2}-\\d{3,4}$", message = "El formato de placa no es válido. Use ABC-123 o ABC-1234.") String placa,

                @NotNull(message = "La fecha y hora son obligatorias.") LocalDateTime fechaHora

) {
}
