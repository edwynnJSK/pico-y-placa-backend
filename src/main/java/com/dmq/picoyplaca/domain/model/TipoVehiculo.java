package com.dmq.picoyplaca.domain.model;

/**
 * Clasificación del vehículo derivada de la segunda letra de la placa.
 */
public enum TipoVehiculo {

    /** Placas cuya segunda letra es A, U o Z */
    COMERCIAL(true),

    /** Placas cuya segunda letra es E */
    GUBERNAMENTAL(true),

    /** Placas cuya segunda letra es X */
    USO_OFICIAL(true),

    /** Placas cuya segunda letra es M */
    GAD(true),

    /** Resto de placas. Sujeto a restricción */
    PRIVADO(false);

    private final boolean potencialmenteExento;

    TipoVehiculo(boolean potencialmenteExento) {
        this.potencialmenteExento = potencialmenteExento;
    }

    /**
     * Indica si este tipo de vehículo podría estar exento
     * 
     * @return True si el tipo sugiere posible exención no verificable.
     */
    public boolean esPotencialmenteExento() {
        return potencialmenteExento;
    }

    /**
     * Deriva el tipo de vehículo a partir de la segunda letra de la placa.
     *
     * @param segundaLetra segunda letra del código alfabético de la placa.
     * @return TipoVehiculo correspondiente.
     */
    public static TipoVehiculo desdeLetra(char segundaLetra) {
        return switch (Character.toUpperCase(segundaLetra)) {
            case 'A', 'U', 'Z' -> COMERCIAL;
            case 'E' -> GUBERNAMENTAL;
            case 'X' -> USO_OFICIAL;
            case 'M' -> GAD;
            default -> PRIVADO;
        };
    }
}
