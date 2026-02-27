package com.dmq.picoyplaca.infrastructure.adapter.input.rest;

import com.dmq.picoyplaca.application.dto.ConsultaCirculacionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CirculacionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /validar — Escenario Feliz: Martes fuera de restricción")
    void debeValidarExitosamente() throws Exception {
        ConsultaCirculacionRequest request = new ConsultaCirculacionRequest(
                "PBX-1234",
                LocalDateTime.of(2026, 3, 10, 12, 0));

        mockMvc.perform(post("/api/v1/circulacion/validar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonResult -> {
                    String content = jsonResult.getResponse().getContentAsString();
                    System.out.println("Response: " + content);
                })
                .andExpect(jsonPath("$.puedeCircular").value(true))
                .andExpect(jsonPath("$.motivo").value("FUERA_FRANJA"));
    }

    @Test
    @DisplayName("POST /validar — Error 400: Placa inválida")
    void debeRetornar400ParaPlacaInvalida() throws Exception {
        ConsultaCirculacionRequest request = new ConsultaCirculacionRequest(
                "AB-12",
                LocalDateTime.of(2026, 3, 10, 7, 30));

        mockMvc.perform(post("/api/v1/circulacion/validar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDACION_FALLIDA"));
    }

    @Test
    @DisplayName("GET /feriados — Público e inicializado por Flyway")
    void debeListarFeriados() throws Exception {
        mockMvc.perform(get("/api/v1/feriados?anio=2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(11))
                .andExpect(jsonPath("$[0].nombre").value("Año Nuevo"));
    }

    @Test
    @DisplayName("POST /feriados — Error 401 Sin Autenticación")
    void debeRetornar401SinAuth() throws Exception {
        mockMvc.perform(post("/api/v1/feriados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fecha\":\"2026-06-01\",\"nombre\":\"Test\",\"tipo\":\"NACIONAL\"}"))
                .andExpect(status().isUnauthorized());
    }
}
