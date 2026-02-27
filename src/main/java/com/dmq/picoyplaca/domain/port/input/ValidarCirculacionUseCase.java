package com.dmq.picoyplaca.domain.port.input;

import com.dmq.picoyplaca.domain.model.ResultadoCirculacion;

import java.time.LocalDateTime;

/**
 * Puerto de entrada para la validación de circulación vehicular.
 * Define el contrato que la capa de infraestructura expone hacia el exterior.
 * Las implementaciones realizada en el servicio.
 */
public interface ValidarCirculacionUseCase {

    /**
     * @param placa     valor de la placa vehicular (ej. "PBX-1234")
     * @param fechaHora fecha y hora a evaluar (presente o futura)
     * @return ResultadoCirculacion con el resultado
     */
    ResultadoCirculacion validar(String placa, LocalDateTime fechaHora);
}
