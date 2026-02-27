package com.dmq.picoyplaca.application.mapper;

import com.dmq.picoyplaca.application.dto.ConsultaCirculacionResponse;
import com.dmq.picoyplaca.domain.model.ResultadoCirculacion;

/**
 * Mapeador entre el resultado de dominio y el DTO de respuesta de la API.
 * Actuando como puente entre dominio y la infraestructura.
 */
public class CirculacionMapper {

    private CirculacionMapper() {
    }

    /**
     * Convierte el ResultadoCirculacion del dominio en el DTO de respuesta
     * de la API.
     *
     * @param resultado resultado del servicio de dominio
     * @return DTO listo para serializar en la respuesta HTTP
     */
    public static ConsultaCirculacionResponse toResponse(ResultadoCirculacion resultado) {
        return new ConsultaCirculacionResponse(
                resultado.placa(),
                resultado.fechaHora(),
                resultado.puedeCircular(),
                resultado.motivo(),
                resultado.descripcion(),
                resultado.tipoVehiculo(),
                resultado.diaSemana(),
                resultado.franjaAplicada(),
                resultado.tieneAdvertenciaExencion(),
                resultado.mensajeAdvertencia(),
                resultado.normativa());
    }
}
