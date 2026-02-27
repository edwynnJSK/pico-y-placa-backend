package com.dmq.picoyplaca.infrastructure.adapter.input.rest;

import com.dmq.picoyplaca.application.dto.ConsultaCirculacionRequest;
import com.dmq.picoyplaca.application.dto.ConsultaCirculacionResponse;
import com.dmq.picoyplaca.application.mapper.CirculacionMapper;
import com.dmq.picoyplaca.domain.model.ResultadoCirculacion;
import com.dmq.picoyplaca.domain.port.input.ValidarCirculacionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para validar la circulación de un vehículo
 */
@RestController
@RequestMapping("/api/v1/circulacion")
@Tag(name = "Circulación", description = "Validación de restricción vehicular Pico y Placa")
public class CirculacionController {

    private final ValidarCirculacionUseCase validarCirculacionUseCase;

    public CirculacionController(ValidarCirculacionUseCase validarCirculacionUseCase) {
        this.validarCirculacionUseCase = validarCirculacionUseCase;
    }

    /**
     * @param request datos de la consulta
     * @return resultado completo de la validación
     */
    @PostMapping("/validar")
    @Operation(summary = "Validar restricción vehicular", description = "Determina si un vehículo puede circular según el plan Pico y Placa vigente", responses = {
            @ApiResponse(responseCode = "200", description = "Validación exitosa"),
            @ApiResponse(responseCode = "400", description = "Placa inválida", content = @Content(schema = @Schema(implementation = com.dmq.picoyplaca.infrastructure.exception.ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Fecha anterior al momento actual", content = @Content(schema = @Schema(implementation = com.dmq.picoyplaca.infrastructure.exception.ErrorResponse.class)))
    })
    public ResponseEntity<ConsultaCirculacionResponse> validar(
            @Valid @RequestBody ConsultaCirculacionRequest request) {

        ResultadoCirculacion resultado = validarCirculacionUseCase.validar(request.placa(), request.fechaHora());

        return ResponseEntity.ok(CirculacionMapper.toResponse(resultado));
    }
}
