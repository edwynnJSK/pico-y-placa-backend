package com.dmq.picoyplaca.infrastructure.adapter.input.rest;

import com.dmq.picoyplaca.infrastructure.adapter.output.persistence.FeriadoEntity;
import com.dmq.picoyplaca.infrastructure.adapter.output.persistence.FeriadoJpaRepository;
import com.dmq.picoyplaca.infrastructure.adapter.output.persistence.FeriadoRepositoryAdapter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para la gestión de feriados
 */
@RestController
@RequestMapping("/api/v1/feriados")
@Tag(name = "Feriados", description = "Administración de feriados nacionales")
public class FeriadosController {

    private final FeriadoJpaRepository feriadoJpaRepository;
    private final FeriadoRepositoryAdapter feriadoRepositoryAdapter;

    public FeriadosController(FeriadoJpaRepository feriadoJpaRepository,
            FeriadoRepositoryAdapter feriadoRepositoryAdapter) {
        this.feriadoJpaRepository = feriadoJpaRepository;
        this.feriadoRepositoryAdapter = feriadoRepositoryAdapter;
    }

    @GetMapping
    @Operation(summary = "Listar feriados por año")
    public ResponseEntity<List<FeriadoResponse>> listar(
            @RequestParam(defaultValue = "0") int anio) {
        int anioConsulta = anio == 0 ? LocalDate.now().getYear() : anio;
        List<FeriadoResponse> respuestas = feriadoJpaRepository.findByAnioAndActivoTrue(anioConsulta)
                .stream()
                .map(FeriadoResponse::de)
                .toList();
        return ResponseEntity.ok(respuestas);
    }

    @PostMapping
    @Operation(summary = "Crear feriado", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<FeriadoResponse> crear(@Valid @RequestBody FeriadoRequest request) {
        FeriadoEntity entidad = new FeriadoEntity(request.fecha(), request.nombre(), request.tipo());
        FeriadoEntity guardada = feriadoJpaRepository.save(entidad);
        feriadoRepositoryAdapter.evictCache();
        return ResponseEntity.status(HttpStatus.CREATED).body(FeriadoResponse.de(guardada));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar feriado", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<FeriadoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FeriadoRequest request) {
        FeriadoEntity entidad = feriadoJpaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Feriado no encontrado con id: " + id));
        entidad.setFecha(request.fecha());
        entidad.setNombre(request.nombre());
        entidad.setTipo(request.tipo());
        FeriadoEntity guardada = feriadoJpaRepository.save(entidad);
        feriadoRepositoryAdapter.evictCache();
        return ResponseEntity.ok(FeriadoResponse.de(guardada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar (desactivar) feriado", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        FeriadoEntity entidad = feriadoJpaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Feriado no encontrado con id: " + id));
        entidad.setActivo(false);
        feriadoJpaRepository.save(entidad);
        feriadoRepositoryAdapter.evictCache();
        return ResponseEntity.noContent().build();
    }

    /**
     * DTO para creación/actualización de feriados.
     */
    public record FeriadoRequest(
            @NotNull(message = "La fecha es obligatoria.") LocalDate fecha,
            @NotBlank(message = "El nombre es obligatorio.") String nombre,
            @NotBlank(message = "El tipo es obligatorio.") String tipo) {
    }

    /**
     * DTO de salida para evitar exponer la entidad de base de datos..
     */
    public record FeriadoResponse(
            Long id,
            LocalDate fecha,
            String nombre,
            String tipo) {

        public static FeriadoResponse de(FeriadoEntity entidad) {
            return new FeriadoResponse(
                    entidad.getId(),
                    entidad.getFecha(),
                    entidad.getNombre(),
                    entidad.getTipo());
        }
    }
}
