package com.dmq.picoyplaca.bdd.steps;

import com.dmq.picoyplaca.domain.model.MotivoResultado;
import com.dmq.picoyplaca.domain.model.Placa;
import com.dmq.picoyplaca.domain.model.ResultadoCirculacion;
import com.dmq.picoyplaca.domain.port.output.FeriadoRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.ReglaRestriccionRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.RelojPort;
import com.dmq.picoyplaca.domain.service.ValidarCirculacionService;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.*;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
@SpringBootTest
public class CirculacionSteps {

    private final ValidarCirculacionService service;

    @MockitoBean
    private FeriadoRepositoryPort feriadoRepository;
    @MockitoBean
    private ReglaRestriccionRepositoryPort reglaRepository;
    @MockitoBean
    private RelojPort reloj;

    private String placaInput;
    private LocalDateTime fechaHoraInput;
    private ResultadoCirculacion resultado;
    private Exception errorCapturado;

    public CirculacionSteps(ValidarCirculacionService service) {
        this.service = service;
    }

    @Dado("que la placa es {string}")
    public void queLaPlacaEs(String placa) {
        this.placaInput = placa;
    }

    @Dado("la fecha y hora es un {string} a las {string}")
    public void laFechaYHoraEsUnALas(String diaOFeriado, String horaStr) {
        LocalTime hora = LocalTime.parse(horaStr);
        LocalDate fecha;

        // Mock del reloj para evitar error de fecha anterior (Step 2 del árbol)
        when(reloj.ahora()).thenReturn(LocalDateTime.of(2026, 1, 1, 0, 0));

        switch (diaOFeriado.toLowerCase()) {
            case "lunes" -> fecha = LocalDate.of(2026, 3, 9);
            case "martes" -> fecha = LocalDate.of(2026, 3, 10);
            case "miércoles" -> fecha = LocalDate.of(2026, 3, 11);
            case "jueves" -> fecha = LocalDate.of(2026, 3, 12);
            case "viernes" -> fecha = LocalDate.of(2026, 3, 13);
            case "sábado" -> fecha = LocalDate.of(2026, 3, 14);
            case "domingo" -> fecha = LocalDate.of(2026, 3, 15);
            case "feriado nacional" -> {
                fecha = LocalDate.of(2026, 5, 25); // Batalla de Pichincha
                when(feriadoRepository.esFeriado(fecha)).thenReturn(true);
            }
            default -> throw new IllegalArgumentException("Día no soportado: " + diaOFeriado);
        }

        this.fechaHoraInput = LocalDateTime.of(fecha, hora);

        // Mock de reglas (excepto para feriados y fines de semana que se evalúan antes)
        if (!diaOFeriado.equals("feriado nacional") && fecha.getDayOfWeek().getValue() < 6) {
            when(reglaRepository.buscarPorDiaYFecha(eq(fecha.getDayOfWeek()), eq(fecha)))
                    .thenReturn(Optional.of(crearReglaParaDia(fecha.getDayOfWeek())));
        }
    }

    @Dado("que la fecha y hora es anterior a ahora")
    public void queLaFechaYHoraEsAnteriorAAhora() {
        LocalDateTime ahora = LocalDateTime.now();
        when(reloj.ahora()).thenReturn(ahora);
        this.fechaHoraInput = ahora.minusHours(1);
        this.placaInput = "ABC-1234";
    }

    @Cuando("se consulta la restricción")
    public void seConsultaLaRestriccion() {
        try {
            resultado = service.validar(placaInput, fechaHoraInput);
            errorCapturado = null;
        } catch (Exception e) {
            errorCapturado = e;
            resultado = null;
        }
    }

    @Entonces("el resultado es RESTRINGIDO")
    public void elResultadoEsRestringido() {
        assertNotNull(resultado);
        assertFalse(resultado.puedeCircular());
    }

    @Entonces("el resultado es LIBRE")
    public void elResultadoEsLibre() {
        assertNotNull(resultado);
        assertTrue(resultado.puedeCircular());
    }

    @Entonces("el motivo es {string}")
    public void elMotivoEs(String motivo) {
        assertEquals(MotivoResultado.valueOf(motivo), resultado.motivo());
    }

    @Entonces("el sistema retorna error {string}")
    public void elSistemaRetornaError(String codigoError) {
        assertNotNull(errorCapturado);
        if (codigoError.equals("PLACA_INVALIDA")) {
            assertTrue(errorCapturado instanceof Placa.PlacaFormatoInvalidoException);
        } else if (codigoError.equals("FECHA_ANTERIOR")) {
            assertTrue(errorCapturado instanceof ValidarCirculacionService.FechaAnteriorException);
        }
    }

    @Entonces("tieneAdvertenciaExencion es verdadero")
    public void tieneAdvertenciaExencionEsVerdadero() {
        assertTrue(resultado.tieneAdvertenciaExencion());
    }

    private com.dmq.picoyplaca.domain.model.ReglaRestriccion crearReglaParaDia(DayOfWeek dia) {
        Set<Integer> digitos = switch (dia) {
            case MONDAY -> Set.of(1, 2);
            case TUESDAY -> Set.of(3, 4);
            case WEDNESDAY -> Set.of(5, 6);
            case THURSDAY -> Set.of(7, 8);
            case FRIDAY -> Set.of(9, 0);
            default -> Set.of();
        };
        return new com.dmq.picoyplaca.domain.model.ReglaRestriccion(
                1L, dia, digitos,
                com.dmq.picoyplaca.domain.model.FranjaHoraria.MANANA,
                com.dmq.picoyplaca.domain.model.FranjaHoraria.TARDE,
                LocalDate.of(2021, 12, 15), null, true);
    }

    private DayOfWeek eq(DayOfWeek dia) {
        return dia;
    }

    private LocalDate eq(LocalDate fecha) {
        return fecha;
    }
}
