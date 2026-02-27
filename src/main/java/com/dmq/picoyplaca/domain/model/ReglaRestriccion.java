package com.dmq.picoyplaca.domain.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entidad de dominio que representa una regla de restricción vehicular para un
 * día de la semana específico.
 * Las reglas se almacenan en base de datos de la tabla reglas_restriccion
 */
public class ReglaRestriccion {

    private Long id;
    private DayOfWeek diaSemana;

    /** Dígitos restringidos para este día, ej. {3, 4} para martes. */
    private Set<Integer> digitosRestringidos;

    private FranjaHoraria franjaManana;
    private FranjaHoraria franjaVespertina;

    private LocalDate vigenciaDesde;

    /** Indica vigencia indefinida. */
    private LocalDate vigenciaHasta;

    private boolean activo;

    public ReglaRestriccion(
            Long id,
            DayOfWeek diaSemana,
            Set<Integer> digitosRestringidos,
            FranjaHoraria franjaManana,
            FranjaHoraria franjaVespertina,
            LocalDate vigenciaDesde,
            LocalDate vigenciaHasta,
            boolean activo) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.digitosRestringidos = digitosRestringidos;
        this.franjaManana = franjaManana;
        this.franjaVespertina = franjaVespertina;
        this.vigenciaDesde = vigenciaDesde;
        this.vigenciaHasta = vigenciaHasta;
        this.activo = activo;
    }

    /**
     * Determina si esta regla está vigente en la fecha dada.
     *
     * @param fecha fecha a evaluar
     * @return True si la regla aplica en esa fecha
     */
    public boolean aplicaEn(LocalDate fecha) {
        if (!activo) {
            return false;
        }
        boolean despuesDeLaVigencia = !fecha.isBefore(vigenciaDesde);
        boolean antesDelFin = vigenciaHasta == null || !fecha.isAfter(vigenciaHasta);
        return despuesDeLaVigencia && antesDelFin;
    }

    /**
     * Evalúa si un dígito dado está restringido por esta regla.
     *
     * @param digito último dígito de la placa
     * @return True si el dígito está en el conjunto restringido
     */
    public boolean restriccionAplicaParaDigito(int digito) {
        return digitosRestringidos.contains(digito);
    }

    /**
     * Determina si la hora dada cae en alguna de las franjas restringidas.
     *
     * @param hora hora a evaluar
     * @return FranjaHoraria que aplica, o null si no hay restricción
     */
    public FranjaHoraria determinarFranjaActiva(LocalTime hora) {
        if (franjaManana.estaEnFranja(hora)) {
            return franjaManana;
        }
        if (franjaVespertina.estaEnFranja(hora)) {
            return franjaVespertina;
        }
        return null;
    }

    /**
     * Parsea una cadena de dígitos CSV a un conjunto de enteros.
     * Ejemplo: "3,4" → {3, 4}
     */
    public static Set<Integer> parsearDigitos(String digitosCsv) {
        return Arrays.stream(digitosCsv.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public Set<Integer> getDigitosRestringidos() {
        return digitosRestringidos;
    }

    public FranjaHoraria getFranjaManana() {
        return franjaManana;
    }

    public FranjaHoraria getFranjaVespertina() {
        return franjaVespertina;
    }

    public LocalDate getVigenciaDesde() {
        return vigenciaDesde;
    }

    public LocalDate getVigenciaHasta() {
        return vigenciaHasta;
    }

    public boolean isActivo() {
        return activo;
    }
}
