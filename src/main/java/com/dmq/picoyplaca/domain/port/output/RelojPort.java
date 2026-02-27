package com.dmq.picoyplaca.domain.port.output;

import java.time.LocalDateTime;

/**
 * Puerto de salida que abstrae el reloj del sistema.
 */
public interface RelojPort {

    /**
     * @return instante actual como LocalDateTime
     */
    LocalDateTime ahora();
}
