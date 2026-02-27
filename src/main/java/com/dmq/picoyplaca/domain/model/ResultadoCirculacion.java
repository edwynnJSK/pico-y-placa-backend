package com.dmq.picoyplaca.domain.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Value Object de salida que encapsula el resultado completo de la validación
 * 
 * @param placa                    valor de la placa consultada
 * @param fechaHora                fecha y hora consultadas
 * @param puedeCircular            True si el vehículo puede circular
 * @param motivo                   razón del resultado
 * @param descripcion              mensaje explicativo legible
 * @param tipoVehiculo             clasificación del vehículo
 * @param diaSemana                día de la semana evaluado
 * @param franjaAplicada           franja horaria que aplica (null si no hay
 *                                 restricción)
 * @param tieneAdvertenciaExencion True si el tipo sugiere posible
 *                                 exención
 * @param mensajeAdvertencia       mensaje de advertencia de posible exención
 *                                 (null si no aplica)
 * @param normativa                referencia a la resolución
 */
public record ResultadoCirculacion(
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
