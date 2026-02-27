package com.dmq.picoyplaca.domain.service;

import com.dmq.picoyplaca.domain.model.FranjaHoraria;
import com.dmq.picoyplaca.domain.model.MotivoResultado;
import com.dmq.picoyplaca.domain.model.Placa;
import com.dmq.picoyplaca.domain.model.ReglaRestriccion;
import com.dmq.picoyplaca.domain.model.ResultadoCirculacion;
import com.dmq.picoyplaca.domain.port.input.ValidarCirculacionUseCase;
import com.dmq.picoyplaca.domain.port.output.FeriadoRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.ReglaRestriccionRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.RelojPort;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Servicio de dominio que implementa la lógica para la validación de
 * circulación
 */
public class ValidarCirculacionService implements ValidarCirculacionUseCase {

    private static final String NORMATIVA = "Resolución SM-2021-0277 — DMQ, vigente desde 2021-12-15";
    private static final String ADVERTENCIA_EXENCION_TEMPLATE = "El vehículo tipo %s podría estar exento si presenta documentación habilitante "
            + "(discapacidad, eléctrico, servicios de emergencia). Verificar con la autoridad competente.";

    private final FeriadoRepositoryPort feriadoRepository;
    private final ReglaRestriccionRepositoryPort reglaRepository;
    private final RelojPort reloj;

    public ValidarCirculacionService(
            FeriadoRepositoryPort feriadoRepository,
            ReglaRestriccionRepositoryPort reglaRepository,
            RelojPort reloj) {
        this.feriadoRepository = feriadoRepository;
        this.reglaRepository = reglaRepository;
        this.reloj = reloj;
    }

    /**
     * Evalúa si el vehículo puede circular
     * 
     * @param placaValor valor de la placa (ej. "PBX-1234")
     * @param fechaHora  fecha y hora a evaluar (debe ser presente o futura)
     * @return resultado completo de la validación
     */
    @Override
    public ResultadoCirculacion validar(String placaValor, LocalDateTime fechaHora) {
        // Paso 1: Validar formato de placa (lanza excepción de dominio si es inválido)
        Placa placa = Placa.parsear(placaValor);

        // Paso 2: Validar que la fecha no sea anterior al momento actual
        validarFechaNoAnterior(fechaHora);

        LocalDate fecha = fechaHora.toLocalDate();
        LocalTime hora = fechaHora.toLocalTime();
        DayOfWeek diaSemana = fecha.getDayOfWeek();

        // Paso 3: Consultar si es feriado
        if (feriadoRepository.esFeriado(fecha)) {
            return construirResultadoLibre(placa, fechaHora, diaSemana, MotivoResultado.FERIADO,
                    "El día " + fecha + " es feriado nacional. El vehículo puede circular libremente.");
        }

        // Paso 4: Consultar si es fin de semana
        if (esFindeSemana(diaSemana)) {
            return construirResultadoLibre(placa, fechaHora, diaSemana, MotivoResultado.FIN_SEMANA,
                    "Los fines de semana no aplica restricción Pico y Placa.");
        }

        // Paso 5: Consultar si la hora está fuera de las franjas restringidas
        Optional<ReglaRestriccion> reglaOpt = reglaRepository.buscarPorDiaYFecha(diaSemana, fecha);

        if (reglaOpt.isEmpty()) {
            return construirResultadoLibre(placa, fechaHora, diaSemana, MotivoResultado.FUERA_FRANJA,
                    "No existe regla de restricción vigente para este día.");
        }

        ReglaRestriccion regla = reglaOpt.get();
        FranjaHoraria franjaActiva = regla.determinarFranjaActiva(hora);

        if (franjaActiva == null) {
            return construirResultadoLibre(placa, fechaHora, diaSemana, MotivoResultado.FUERA_FRANJA,
                    "La hora " + formatearHora(hora) + " está fuera de las franjas de restricción.");
        }

        // Paso 6: Consultar si el dígito de la placa está en los dígitos restringidos
        // del día
        if (!regla.restriccionAplicaParaDigito(placa.ultimoDigito())) {
            return construirResultadoLibre(placa, fechaHora, diaSemana, MotivoResultado.DIGITO_NO_APLICA,
                    "El dígito " + placa.ultimoDigito() + " no tiene restricción el día "
                            + traducirDia(diaSemana) + ".");
        }

        // Restriction activa donde el vehículo no puede circular
        return construirResultadoRestringido(placa, fechaHora, diaSemana, franjaActiva);
    }

    // ── Métodos privados de construcción ─────────────────────────────────────

    private ResultadoCirculacion construirResultadoLibre(
            Placa placa,
            LocalDateTime fechaHora,
            DayOfWeek diaSemana,
            MotivoResultado motivo,
            String descripcion) {

        boolean tieneAdvertencia = placa.tipo().esPotencialmenteExento();
        String mensajeAdvertencia = tieneAdvertencia
                ? String.format(ADVERTENCIA_EXENCION_TEMPLATE, placa.tipo())
                : null;

        return new ResultadoCirculacion(
                placa.valor(),
                fechaHora,
                true,
                motivo,
                descripcion,
                placa.tipo(),
                diaSemana,
                null,
                tieneAdvertencia,
                mensajeAdvertencia,
                NORMATIVA);
    }

    private ResultadoCirculacion construirResultadoRestringido(
            Placa placa,
            LocalDateTime fechaHora,
            DayOfWeek diaSemana,
            FranjaHoraria franja) {

        String descripcion = String.format(
                "El vehículo con placa %s tiene restricción activa. "
                        + "El dígito %d tiene restricción los %s en la franja %s.",
                placa.valor(),
                placa.ultimoDigito(),
                traducirDia(diaSemana),
                franja.formatoLegible());

        boolean tieneAdvertencia = placa.tipo().esPotencialmenteExento();
        String mensajeAdvertencia = tieneAdvertencia
                ? String.format(ADVERTENCIA_EXENCION_TEMPLATE, placa.tipo())
                : null;

        return new ResultadoCirculacion(
                placa.valor(),
                fechaHora,
                false,
                MotivoResultado.RESTRICCION_ACTIVA,
                descripcion,
                placa.tipo(),
                diaSemana,
                franja.formatoLegible(),
                tieneAdvertencia,
                mensajeAdvertencia,
                NORMATIVA);
    }

    private void validarFechaNoAnterior(LocalDateTime fechaHora) {
        LocalDateTime ahora = reloj.ahora().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
        LocalDateTime consulta = fechaHora.truncatedTo(java.time.temporal.ChronoUnit.MINUTES);

        if (consulta.isBefore(ahora)) {
            throw new FechaAnteriorException(fechaHora);
        }
    }

    private boolean esFindeSemana(DayOfWeek dia) {
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    private String formatearHora(LocalTime hora) {
        return String.format("%02d:%02d", hora.getHour(), hora.getMinute());
    }

    private String traducirDia(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "lunes";
            case TUESDAY -> "martes";
            case WEDNESDAY -> "miércoles";
            case THURSDAY -> "jueves";
            case FRIDAY -> "viernes";
            case SATURDAY -> "sábado";
            case SUNDAY -> "domingo";
        };
    }

    /**
     * Excepción de dominio cuando la fecha consultada es anterior al instante
     * actual.
     */
    public static final class FechaAnteriorException extends RuntimeException {
        private final LocalDateTime fechaConsultada;

        public FechaAnteriorException(LocalDateTime fechaConsultada) {
            super("La fecha consultada " + fechaConsultada + " es anterior al momento actual.");
            this.fechaConsultada = fechaConsultada;
        }

        public LocalDateTime getFechaConsultada() {
            return fechaConsultada;
        }
    }
}
