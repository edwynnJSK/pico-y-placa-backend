package com.dmq.picoyplaca.domain;

import com.dmq.picoyplaca.domain.model.Placa;
import com.dmq.picoyplaca.domain.model.TipoVehiculo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PlacaTest {

    @ParameterizedTest
    @DisplayName("Debe parsear correctamente placas válidas de 3 y 4 dígitos")
    @CsvSource({
            "ABC-123, 3, PRIVADO",
            "PBX-1234, 4, PRIVADO",
            "AAA-123, 3, COMERCIAL",
            "AEE-123, 3, GUBERNAMENTAL",
            "AXA-123, 3, USO_OFICIAL",
            "AMA-123, 3, GAD"
    })
    void debeParsearPlacasValidas(String valor, int ultimoDigito, TipoVehiculo tipo) {
        Placa placa = Placa.parsear(valor);
        assertEquals(valor.toUpperCase(), placa.valor());
        assertEquals(ultimoDigito, placa.ultimoDigito());
        assertEquals(tipo, placa.tipo());
    }

    @ParameterizedTest
    @DisplayName("Debe lanzar excepción para formatos de placa inválidos")
    @ValueSource(strings = {
            "AB-123", // Faltan letras
            "ABCD-123", // Sobran letras
            "ABC-12", // Faltan números
            "ABC-12345", // Sobran números
            "ABC 123", // Sin guion
            "123-ABC" // Orden invertido
    })
    void debeLanzarExcepcionParaPlacasInvalidas(String valor) {
        assertThrows(Placa.PlacaFormatoInvalidoException.class, () -> Placa.parsear(valor));
    }

    @Test
    @DisplayName("Debe normalizar a mayúsculas al parsear")
    void debeNormalizarAMayusculas() {
        Placa placa = Placa.parsear("pbx-1234");
        assertEquals("PBX-1234", placa.valor());
    }

    @ParameterizedTest
    @DisplayName("Debe identificar el tipo de vehículo por la segunda letra")
    @CsvSource({
            "AAX-123, COMERCIAL",
            "AUX-123, COMERCIAL",
            "AZX-123, COMERCIAL",
            "AEX-123, GUBERNAMENTAL",
            "AXX-123, USO_OFICIAL",
            "AMX-123, GAD",
            "ABX-123, PRIVADO"
    })
    void debeIdentificarTipoVehiculo(String valor, TipoVehiculo tipoEsperado) {
        Placa placa = Placa.parsear(valor);
        assertEquals(tipoEsperado, placa.tipo());
    }

    @Test
    @DisplayName("Debe lanzar excepción para nulo o vacío")
    void debeLanzarExcepcionParaNuloOVacio() {
        assertThrows(Placa.PlacaFormatoInvalidoException.class, () -> Placa.parsear(null));
        assertThrows(Placa.PlacaFormatoInvalidoException.class, () -> Placa.parsear("   "));
    }

    @Test
    @DisplayName("Debe permitir acceder al valor inválido en la excepción")
    void debePermitirAccederAlValorInvalido() {
        Placa.PlacaFormatoInvalidoException ex = assertThrows(Placa.PlacaFormatoInvalidoException.class,
                () -> Placa.parsear("INVALIDO"));
        assertEquals("INVALIDO", ex.getValorInvalido());
    }
}
