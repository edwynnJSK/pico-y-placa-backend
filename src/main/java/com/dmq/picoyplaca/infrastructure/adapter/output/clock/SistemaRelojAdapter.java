package com.dmq.picoyplaca.infrastructure.adapter.output.clock;

import com.dmq.picoyplaca.domain.port.output.RelojPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Adaptador del reloj del sistema que implementa {@link RelojPort}.
 */
@Component
public class SistemaRelojAdapter implements RelojPort {

    private final ZoneId zonaHoraria;

    public SistemaRelojAdapter(@Value("${app.timezone:America/Guayaquil}") String timezone) {
        this.zonaHoraria = ZoneId.of(timezone);
    }

    @Override
    public LocalDateTime ahora() {
        return LocalDateTime.now(zonaHoraria);
    }
}
