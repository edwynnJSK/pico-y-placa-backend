package com.dmq.picoyplaca.bdd.config;

import com.dmq.picoyplaca.domain.port.output.FeriadoRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.ReglaRestriccionRepositoryPort;
import com.dmq.picoyplaca.domain.port.output.RelojPort;
import com.dmq.picoyplaca.domain.service.ValidarCirculacionService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class CucumberSpringConfiguration {

    @Bean
    @Primary
    public FeriadoRepositoryPort feriadoRepositoryPort() {
        return Mockito.mock(FeriadoRepositoryPort.class);
    }

    @Bean
    @Primary
    public ReglaRestriccionRepositoryPort reglaRestriccionRepositoryPort() {
        return Mockito.mock(ReglaRestriccionRepositoryPort.class);
    }

    @Bean
    @Primary
    public RelojPort relojPort() {
        return Mockito.mock(RelojPort.class);
    }

    @Bean
    public ValidarCirculacionService validarCirculacionService(
            FeriadoRepositoryPort feriadoRepository,
            ReglaRestriccionRepositoryPort reglaRepository,
            RelojPort reloj) {
        return new ValidarCirculacionService(feriadoRepository, reglaRepository, reloj);
    }
}
