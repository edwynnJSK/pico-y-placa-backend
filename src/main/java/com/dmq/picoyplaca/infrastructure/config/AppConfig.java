package com.dmq.picoyplaca.infrastructure.config;

import com.dmq.picoyplaca.domain.port.output.FeriadoRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.ReglaRestriccionRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.RelojPort;
import com.dmq.picoyplaca.domain.service.ValidarCirculacionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Beans de dominio y enlace a adaptadores de infraestructura
 * Inyección de adaptadores de infraestructura para el servicio de validaación
 */
@Configuration
public class AppConfig {

    @Bean
    public ValidarCirculacionService validarCirculacionService(
            FeriadoRepositoryPort feriadoRepositoryPort,
            ReglaRestriccionRepositoryPort reglaRestriccionRepositoryPort,
            RelojPort relojPort) {
        return new ValidarCirculacionService(
                feriadoRepositoryPort,
                reglaRestriccionRepositoryPort,
                relojPort);
    }
}
