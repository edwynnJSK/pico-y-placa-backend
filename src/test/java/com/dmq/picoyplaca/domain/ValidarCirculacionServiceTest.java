package com.dmq.picoyplaca.domain;

import com.dmq.picoyplaca.domain.model.*;
import com.dmq.picoyplaca.domain.port.output.FeriadoRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.ReglaRestriccionRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.RelojPort;
import com.dmq.picoyplaca.domain.service.ValidarCirculacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarCirculacionServiceTest {

    @Mock
    private FeriadoRepositoryPort feriadoRepository;
    @Mock
    private ReglaRestriccionRepositoryPort reglaRepository;
    @Mock
    private RelojPort reloj;

    private ValidarCirculacionService service;

    private final LocalDateTime AHORA = LocalDateTime.of(2025, 1, 1, 10, 0);

    @BeforeEach
    void setUp() {
        service = new ValidarCirculacionService(feriadoRepository, reglaRepository, reloj);
        lenient().when(reloj.ahora()).thenReturn(AHORA);
    }

    @Test
    @DisplayName("Escenario 3: Día feriado (Libre con motivo FERIADO)")
    void debePermitirCircularEnFeriado() {
        LocalDate fecha = LocalDate.of(2026, 1, 1);
        LocalDateTime consulta = LocalDateTime.of(fecha, LocalTime.of(8, 0));

        when(feriadoRepository.esFeriado(fecha)).thenReturn(true);

        ResultadoCirculacion res = service.validar("ABC-123", consulta);

        assertTrue(res.puedeCircular());
        assertEquals(MotivoResultado.FERIADO, res.motivo());
        verify(feriadoRepository).esFeriado(fecha);
    }

    @Test
    @DisplayName("Escenario 4: Fin de semana (Libre con motivo FIN_SEMANA)")
    void debePermitirCircularFinDeSemana() {
        LocalDate sabado = LocalDate.of(2026, 2, 28);
        LocalDateTime consulta = LocalDateTime.of(sabado, LocalTime.of(8, 0));

        when(feriadoRepository.esFeriado(sabado)).thenReturn(false);

        ResultadoCirculacion res = service.validar("ABC-123", consulta);

        assertTrue(res.puedeCircular());
        assertEquals(MotivoResultado.FIN_SEMANA, res.motivo());
    }

    @Test
    @DisplayName("Escenario 2: Fuera de franja (Libre con motivo FUERA_FRANJA)")
    void debePermitirCircularFueraDeFranja() {
        LocalDate martes = LocalDate.of(2026, 3, 10);
        LocalDateTime consulta = LocalDateTime.of(martes, LocalTime.of(12, 0));

        when(feriadoRepository.esFeriado(martes)).thenReturn(false);
        when(reglaRepository.buscarPorDiaYFecha(eq(DayOfWeek.TUESDAY), eq(martes)))
                .thenReturn(Optional.of(crearReglaMartes()));

        ResultadoCirculacion res = service.validar("ABC-1234", consulta);

        assertTrue(res.puedeCircular());
        assertEquals(MotivoResultado.FUERA_FRANJA, res.motivo());
    }

    @Test
    @DisplayName("Escenario 5: Dígito no aplica (Libre con motivo DIGITO_NO_APLICA)")
    void debePermitirCircularSiDigitoNoAplica() {
        LocalDate martes = LocalDate.of(2026, 3, 10);
        LocalDateTime consulta = LocalDateTime.of(martes, LocalTime.of(7, 30));

        when(feriadoRepository.esFeriado(martes)).thenReturn(false);
        when(reglaRepository.buscarPorDiaYFecha(eq(DayOfWeek.TUESDAY), eq(martes)))
                .thenReturn(Optional.of(crearReglaMartes()));

        // Placa termina en 1 (aplica lunes), consulta es martes
        ResultadoCirculacion res = service.validar("ABC-1231", consulta);

        assertTrue(res.puedeCircular());
        assertEquals(MotivoResultado.DIGITO_NO_APLICA, res.motivo());
    }

    @Test
    @DisplayName("Escenarios 1 y 8: Restricción activa (RESTRINGIDO)")
    void debeRestringirEnFranjaActiva() {
        LocalDate martes = LocalDate.of(2026, 3, 10);
        LocalDateTime consulta = LocalDateTime.of(martes, LocalTime.of(7, 30));

        when(feriadoRepository.esFeriado(martes)).thenReturn(false);
        when(reglaRepository.buscarPorDiaYFecha(eq(DayOfWeek.TUESDAY), eq(martes)))
                .thenReturn(Optional.of(crearReglaMartes()));

        // Placa termina en 4 (aplica martes), consulta es martes 07:30
        ResultadoCirculacion res = service.validar("ABC-1234", consulta);

        assertFalse(res.puedeCircular());
        assertEquals(MotivoResultado.RESTRICCION_ACTIVA, res.motivo());
        assertEquals("06:00\u201309:30", res.franjaAplicada());
    }

    @Test
    @DisplayName("Escenario 7: Error por fecha anterior (FECHA_ANTERIOR)")
    void debeLanzarExcepcionFechaAnterior() {
        LocalDateTime pasada = AHORA.minusHours(1);

        assertThrows(ValidarCirculacionService.FechaAnteriorException.class,
                () -> service.validar("ABC-1234", pasada));
    }

    @Test
    @DisplayName("Escenario 10: Vehículo comercial con advertencia de exención")
    void debeIncluirAdvertenciaParaVehiculoComercial() {
        LocalDate martes = LocalDate.of(2026, 3, 10); // Martes
        LocalDateTime consulta = LocalDateTime.of(martes, LocalTime.of(7, 30));

        when(feriadoRepository.esFeriado(martes)).thenReturn(false);
        when(reglaRepository.buscarPorDiaYFecha(any(), any())).thenReturn(Optional.of(crearReglaMartes()));

        // Placa AAA (segunda letra A = COMERCIAL)
        ResultadoCirculacion res = service.validar("AAA-1234", consulta);

        assertTrue(res.tieneAdvertenciaExencion());
        assertNotNull(res.mensajeAdvertencia());
        assertTrue(res.mensajeAdvertencia().contains("COMERCIAL"));
    }

    private ReglaRestriccion crearReglaMartes() {
        return new ReglaRestriccion(
                1L,
                DayOfWeek.TUESDAY,
                Set.of(3, 4),
                FranjaHoraria.MANANA,
                FranjaHoraria.TARDE,
                LocalDate.of(2021, 12, 15),
                null,
                true);
    }

    @Test
    @DisplayName("Escenario: Regla no encontrada (Libre con motivo FUERA_FRANJA)")
    void debePermitirCircularSiNoHayRegla() {
        LocalDate martes = LocalDate.of(2026, 3, 10);
        LocalDateTime consulta = LocalDateTime.of(martes, LocalTime.of(7, 30));

        when(feriadoRepository.esFeriado(martes)).thenReturn(false);
        when(reglaRepository.buscarPorDiaYFecha(any(), any())).thenReturn(Optional.empty());

        ResultadoCirculacion res = service.validar("ABC-1234", consulta);

        assertTrue(res.puedeCircular());
        assertEquals(MotivoResultado.FUERA_FRANJA, res.motivo());
    }

    @Test
    @DisplayName("Debe permitir acceder a la fecha consultada en la excepción")
    void debePermitirAccederALaFechaEnLaExcepcion() {
        LocalDateTime pasada = AHORA.minusHours(1);
        ValidarCirculacionService.FechaAnteriorException ex = assertThrows(
                ValidarCirculacionService.FechaAnteriorException.class,
                () -> service.validar("ABC-1234", pasada));
        assertEquals(pasada, ex.getFechaConsultada());
    }
}
