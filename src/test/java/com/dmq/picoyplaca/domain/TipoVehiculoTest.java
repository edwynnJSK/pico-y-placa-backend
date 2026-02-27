package com.dmq.picoyplaca.domain;

import com.dmq.picoyplaca.domain.model.TipoVehiculo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TipoVehiculoTest {

    @ParameterizedTest
    @DisplayName("Debe validar qué tipos son potencialmente exentos")
    @CsvSource({
            "COMERCIAL, true",
            "GUBERNAMENTAL, true",
            "USO_OFICIAL, true",
            "GAD, true",
            "PRIVADO, false"
    })
    void debeValidarExencion(TipoVehiculo tipo, boolean esperado) {
        assertEquals(esperado, tipo.esPotencialmenteExento());
    }

    @ParameterizedTest
    @DisplayName("Debe parsear desde letra correctamente")
    @CsvSource({
            "A, COMERCIAL",
            "U, COMERCIAL",
            "Z, COMERCIAL",
            "E, GUBERNAMENTAL",
            "X, USO_OFICIAL",
            "M, GAD",
            "B, PRIVADO",
            "Q, PRIVADO"
    })
    void debeParsearDesdeLetra(char letra, TipoVehiculo esperado) {
        assertEquals(esperado, TipoVehiculo.desdeLetra(letra));
    }
}
