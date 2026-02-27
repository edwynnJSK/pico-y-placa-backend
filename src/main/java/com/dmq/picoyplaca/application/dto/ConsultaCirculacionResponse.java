package com.dmq.picoyplaca.application.dto;

import com.dmq.picoyplaca.domain.model.MotivoResultado;
import com.dmq.picoyplaca.domain.model.TipoVehiculo;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * DTO de salida que expone el resultado del endpoint despues de validar la
 * circulación.
 * Mapea directamente desde al modelo de ResultadoCirculación..
 *
 * @param placa                    placa consultada
 * @param fechaHora                fecha y hora evaluadas
 * @param puedeCircular            si el vehículo puede circular
 * @param motivo                   razón del resultado
 * @param descripcion              mensaje explicativo
 * @param tipoVehiculo             clasificación del vehículo
 * @param diaSemana                día de la semana evaluado
 * @param franjaAplicada           franja horaria de restricción (null si no
 *                                 aplica)
 * @param tieneAdvertenciaExencion si hay advertencia de posible exención
 * @param mensajeAdvertencia       texto de la advertencia (null si no aplica)
 * @param normativa                referencia a la resolución vigente
 */
public record ConsultaCirculacionResponse(
                String placa,
                LocalDateTime fechaHora,
                boolean puedeCircular,
                MotivoResultado motivo,
                String descripcion,
                TipoVehiculo tipoVehiculo,
                DayOfWeek diaSemana,
                String franjaAplicada,
                boolean tieneAdvertenciaExencion,
                String mensajeAdvertencia,
                String normativa) {
}
