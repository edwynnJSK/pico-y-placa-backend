package com.dmq.picoyplaca.infrastructure.adapter.input.rest;

import com.dmq.picoyplaca.domain.model.Placa;
import com.dmq.picoyplaca.domain.service.ValidarCirculacionService;
import com.dmq.picoyplaca.infrastructure.exception.ErrorResponse;
import com.dmq.picoyplaca.infrastructure.exception.FechaInvalidaException;
import com.dmq.picoyplaca.infrastructure.exception.PlacaInvalidaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Centraliza la gestión de excepciones de la API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        /** Placa con formato inválido detectada por dominio → HTTP 400. */
        @ExceptionHandler(Placa.PlacaFormatoInvalidoException.class)
        public ResponseEntity<ErrorResponse> handlePlacaFormatoInvalido(
                        Placa.PlacaFormatoInvalidoException ex) {
                log.warn("Placa inválida recibida: {}", ex.getValorInvalido());
                return ResponseEntity.badRequest().body(new ErrorResponse(
                                "PLACA_INVALIDA",
                                "El formato de placa '" + ex.getValorInvalido()
                                                + "' no es válido. Formatos aceptados: ABC-123 o ABC-1234.",
                                LocalDateTime.now()));
        }

        /** Placa inválida lanzada desde infraestructura → HTTP 400. */
        @ExceptionHandler(PlacaInvalidaException.class)
        public ResponseEntity<ErrorResponse> handlePlacaInvalida(PlacaInvalidaException ex) {
                log.warn("Placa inválida: {}", ex.getValorInvalido());
                return ResponseEntity.badRequest().body(new ErrorResponse(
                                "PLACA_INVALIDA",
                                ex.getMessage(),
                                LocalDateTime.now()));
        }

        /** Fecha anterior al momento actual → HTTP 422. */
        @ExceptionHandler(ValidarCirculacionService.FechaAnteriorException.class)
        public ResponseEntity<ErrorResponse> handleFechaAnterior(
                        ValidarCirculacionService.FechaAnteriorException ex) {
                log.warn("Fecha anterior consultada: {}", ex.getFechaConsultada());
                return ResponseEntity.unprocessableEntity().body(new ErrorResponse(
                                "FECHA_ANTERIOR",
                                "La fecha y hora ingresada es anterior al momento actual. "
                                                + "Solo se permiten consultas presentes o futuras.",
                                LocalDateTime.now()));
        }

        /** Fecha inválida de infraestructura → HTTP 422. */
        @ExceptionHandler(FechaInvalidaException.class)
        public ResponseEntity<ErrorResponse> handleFechaInvalida(FechaInvalidaException ex) {
                return ResponseEntity.unprocessableEntity().body(new ErrorResponse(
                                "FECHA_ANTERIOR",
                                ex.getMessage(),
                                LocalDateTime.now()));
        }

        /** Validación Bean Validation fallida (@Valid) → HTTP 400. */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex) {
                String mensaje = ex.getBindingResult().getFieldErrors().stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.joining("; "));
                return ResponseEntity.badRequest().body(new ErrorResponse(
                                "VALIDACION_FALLIDA",
                                mensaje,
                                LocalDateTime.now()));
        }

        /** JSON malformado o tipos incorrectos → HTTP 400. */
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleMensajeIlegible(HttpMessageNotReadableException ex) {
                log.warn("Mensaje HTTP ilegible: {}", ex.getMessage());
                return ResponseEntity.badRequest().body(new ErrorResponse(
                                "SOLICITUD_INVALIDA",
                                "El cuerpo de la solicitud no tiene el formato esperado. "
                                                + "Verifique que fechaHora sea similar a 2026-03-10T07:30:00.",
                                LocalDateTime.now()));
        }

        /** Cualquier excepción no manejada → HTTP 500 */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenerico(Exception ex) {
                log.error("Error interno no esperado", ex);
                return ResponseEntity.internalServerError().body(new ErrorResponse(
                                "ERROR_INTERNO",
                                "Ocurrió un error interno. Por favor intente más tarde.",
                                LocalDateTime.now()));
        }
}
