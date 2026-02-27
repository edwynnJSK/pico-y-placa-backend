package com.dmq.picoyplaca.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de documentación con Swagger.
 */
@Configuration
public class OpenApiConfig {

        @Value("${app.normativa.resolucion:SM-2021-0277}")
        private String resolucion;

        @Value("${app.normativa.vigente-desde:2021-12-15}")
        private String vigenciaDesde;

        @Bean
        public OpenAPI openApiPersonalizado() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Pico y Placa API")
                                                .version("1.0.0")
                                                .description("API REST para validación vehicular según la Resolución "
                                                                + resolucion + ", vigente desde " + vigenciaDesde
                                                                + ".\n\n"
                                                                + "**Endpoints de administración** (feriados POST/PUT/DELETE) "
                                                                + "requieren autenticación HTTP Basic.")
                                                .license(new License()
                                                                .name("Uso público")))
                                .components(new Components()
                                                .addSecuritySchemes("basicAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("basic")));
        }
}
