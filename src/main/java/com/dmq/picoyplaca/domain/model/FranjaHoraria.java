package com.dmq.picoyplaca.domain.model;

import java.time.LocalTime;

/**
 * Value Object que representa una franja horaria de restricción.
 * 
 * @param inicio hora de inicio de la franja (inclusivo)
 * @param fin    hora de fin de la franja (inclusivo)
 */
public record FranjaHoraria(LocalTime inicio, LocalTime fin) {

    public static final FranjaHoraria MANANA = new FranjaHoraria(
            LocalTime.of(6, 0),
            LocalTime.of(9, 30));

    public static final FranjaHoraria TARDE = new FranjaHoraria(
            LocalTime.of(16, 0),
            LocalTime.of(20, 0));

    /**
     * Determina si una hora dada cae dentro de esta franja, incluyendo los
     * extremos.
     * 
     * @param hora la hora a evaluar
     * @return True si inicio <= hora <= fin
     */
    public boolean estaEnFranja(LocalTime hora) {
        return !hora.isBefore(inicio) && !hora.isAfter(fin);
    }

    /**
     * Formateador para respuesta del mensaje.
     *
     * @return cadena con formato "HH:mm–HH:mm"
     */
    public String formatoLegible() {
        return String.format("%s\u2013%s",
                formatearHora(inicio),
                formatearHora(fin));
    }

    private String formatearHora(LocalTime hora) {
        return String.format("%02d:%02d", hora.getHour(), hora.getMinute());
    }
}
