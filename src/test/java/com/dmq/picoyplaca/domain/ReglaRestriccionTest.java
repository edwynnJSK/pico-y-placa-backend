package com.dmq.picoyplaca.domain;

import com.dmq.picoyplaca.domain.model.FranjaHoraria;
import com.dmq.picoyplaca.domain.model.ReglaRestriccion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReglaRestriccionTest {

    @Test
    @DisplayName("Debe validar vigencia correctamente")
    void debeValidarVigencia() {
        ReglaRestriccion regla = new ReglaRestriccion(
                1L, DayOfWeek.MONDAY, Set.of(1, 2),
                FranjaHoraria.MANANA, FranjaHoraria.TARDE,
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 12, 31),
                true);

        assertTrue(regla.aplicaEn(LocalDate.of(2021, 6, 1)));
        assertFalse(regla.aplicaEn(LocalDate.of(2020, 12, 31)));
        assertFalse(regla.aplicaEn(LocalDate.of(2022, 1, 1)));
    }

    @Test
    @DisplayName("Debe retornar falso si la regla no está activa")
    void debeRetornarFalsoSiNoEstaActiva() {
        ReglaRestriccion regla = new ReglaRestriccion(
                1L, DayOfWeek.MONDAY, Set.of(1, 2),
                FranjaHoraria.MANANA, FranjaHoraria.TARDE,
                LocalDate.of(2021, 1, 1), null, false);
        assertFalse(regla.aplicaEn(LocalDate.of(2021, 6, 1)));
    }

    @Test
    @DisplayName("Debe determinar franja activa correctamente")
    void debeDeterminarFranjaActiva() {
        ReglaRestriccion regla = new ReglaRestriccion(
                1L, DayOfWeek.MONDAY, Set.of(1, 2),
                FranjaHoraria.MANANA, FranjaHoraria.TARDE,
                LocalDate.of(2021, 1, 1), null, true);

        assertEquals(FranjaHoraria.MANANA, regla.determinarFranjaActiva(LocalTime.of(7, 0)));
        assertEquals(FranjaHoraria.TARDE, regla.determinarFranjaActiva(LocalTime.of(18, 0)));
        assertNull(regla.determinarFranjaActiva(LocalTime.of(12, 0)));
    }

    @Test
    @DisplayName("Debe validar si la restricción aplica para un dígito")
    void debeValidarDigito() {
        ReglaRestriccion regla = new ReglaRestriccion(
                1L, DayOfWeek.MONDAY, Set.of(1, 2),
                FranjaHoraria.MANANA, FranjaHoraria.TARDE,
                LocalDate.of(2021, 1, 1), null, true);
        assertTrue(regla.restriccionAplicaParaDigito(1));
        assertTrue(regla.restriccionAplicaParaDigito(2));
        assertFalse(regla.restriccionAplicaParaDigito(3));
    }
}
