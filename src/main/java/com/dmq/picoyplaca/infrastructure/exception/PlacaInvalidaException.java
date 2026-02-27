package com.dmq.picoyplaca.infrastructure.exception;

/**
 * Excepción de infraestructura lanzada cuando el formato de la placa no es
 * válido.
 * Se origina desde la capa de dominio en el modelo Placa
 * y es manejada por el GlobalExceptionHandler.
 */
public class PlacaInvalidaException extends RuntimeException {

    private final String valorInvalido;

    public PlacaInvalidaException(String valorInvalido) {
        super("El formato de placa '" + valorInvalido + "' no es válido. "
                + "Formatos aceptados: ABC-123 o ABC-1234.");
        this.valorInvalido = valorInvalido;
    }

    public PlacaInvalidaException(String valorInvalido, Throwable cause) {
        super("El formato de placa '" + valorInvalido + "' no es válido. "
                + "Formatos aceptados: ABC-123 o ABC-1234.", cause);
        this.valorInvalido = valorInvalido;
    }

    public String getValorInvalido() {
        return valorInvalido;
    }
}
