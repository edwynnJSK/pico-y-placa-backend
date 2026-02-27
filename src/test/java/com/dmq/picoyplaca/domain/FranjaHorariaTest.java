package com.dmq.picoyplaca.domain;

import com.dmq.picoyplaca.domain.model.FranjaHoraria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class FranjaHorariaTest {

    @ParameterizedTest
    @DisplayName("Debe validar si una hora está en la franja (incluyendo límites)")
    @CsvSource({
            "06:00, 09:30, 06:00, true",
            "06:00, 09:30, 09:30, true",
            "06:00, 09:30, 07:30, true",
            "06:00, 09:30, 05:59, false",
            "06:00, 09:30, 09:31, false",
            "16:00, 21:00, 16:00, true",
            "16:00, 21:00, 21:00, true",
            "16:00, 21:00, 18:00, true",
            "16:00, 21:00, 15:59, false",
            "16:00, 21:00, 21:01, false"
    })
    void debeValidarHoraEnFranja(String inicio, String fin, String hora, boolean esperado) {
        FranjaHoraria franja = new FranjaHoraria(LocalTime.parse(inicio), LocalTime.parse(fin));
        assertEquals(esperado, franja.estaEnFranja(LocalTime.parse(hora)));
    }

    @ParameterizedTest
    @DisplayName("Debe generar formato legible")
    @CsvSource({
            "06:00, 09:30, 06:00–09:30",
            "16:00, 21:00, 16:00–21:00"
    })
    void debeGenerarFormatoLegible(String inicio, String fin, String esperado) {
        FranjaHoraria franja = new FranjaHoraria(LocalTime.parse(inicio), LocalTime.parse(fin));
        assertEquals(esperado, franja.formatoLegible());
    }
}
