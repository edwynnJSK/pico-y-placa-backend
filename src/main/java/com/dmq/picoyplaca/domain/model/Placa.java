package com.dmq.picoyplaca.domain.model;

import java.util.regex.Pattern;

/**
 * Value Object que representa una placa vehicular ecuatoriana.
 * 
 * @param valor        cadena original de la placa, ej. "PBX-1234"
 * @param ultimoDigito último dígito numérico, ej. 4
 * @param tipo         clasificación derivada de la segunda letra
 */
public record Placa(String valor, int ultimoDigito, TipoVehiculo tipo) {

    /** Patrón combinado que acepta 3 o 4 dígitos numéricos. */
    private static final Pattern PATRON_PLACA = Pattern.compile("^[ABCEGHIJKLMNOPQRSTUVWXZ][A-Z]{2}-\\d{3,4}$");

    /**
     * Parsea y valida una cadena como placa.
     * La validación se normaliza a mayúsculas antes de aplicar el patrón.
     *
     * @param valor cadena a parsear (ej. "pBx-1234" o "PBX-1234")
     * @return instancia válida de Placa
     * @throws com.dmq.picoyplaca.infrastructure.exception.PlacaInvalidaException
     */
    public static Placa parsear(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new PlacaFormatoInvalidoException(valor);
        }

        String normalizado = valor.trim().toUpperCase();

        if (!PATRON_PLACA.matcher(normalizado).matches()) {
            throw new PlacaFormatoInvalidoException(normalizado);
        }

        // Parte numérica está después del guion
        String parteNumerica = normalizado.substring(4);
        int ultimoDigito = Character.getNumericValue(parteNumerica.charAt(parteNumerica.length() - 1));

        // Segunda letra para determina el tipo de vehículo
        char segundaLetra = normalizado.charAt(1);
        TipoVehiculo tipo = TipoVehiculo.desdeLetra(segundaLetra);

        return new Placa(normalizado, ultimoDigito, tipo);
    }

    /**
     * Excepción interna de dominio, lanzada cuando el formato de placa no es
     * válido.
     * Se convierte PlacaInvalidaException en la capa de infraestructura.
     */
    public static final class PlacaFormatoInvalidoException extends RuntimeException {
        private final String valorInvalido;

        public PlacaFormatoInvalidoException(String valorInvalido) {
            super("Formato de placa inválido: " + valorInvalido);
            this.valorInvalido = valorInvalido;
        }

        public String getValorInvalido() {
            return valorInvalido;
        }
    }
}
