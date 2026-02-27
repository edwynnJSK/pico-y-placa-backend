package com.dmq.picoyplaca.domain.model;

/**
 * Razón por la cual un vehículo puede o no circular.
 * Usado en ResultadoCirculacion
 */
public enum MotivoResultado {

    /** El día consultado es feriado nacional. Tiene prioridad sobre todo. */
    FERIADO,

    /** El día consultado cae en sábado o domingo. */
    FIN_SEMANA,

    /** La hora consultada está fuera de las franjas horarias restringidas. */
    FUERA_FRANJA,

    /** El último dígito de la placa no corresponde al día consultado. */
    DIGITO_NO_APLICA,

    /** El vehículo está dentro de la restricción activa (dígito + franja + día). */
    RESTRICCION_ACTIVA
}
