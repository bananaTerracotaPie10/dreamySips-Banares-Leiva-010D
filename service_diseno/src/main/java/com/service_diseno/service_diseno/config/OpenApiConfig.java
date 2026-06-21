package com.service_diseno.service_diseno.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dreamySipsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                         .title("DreamySips - Diseño API")
                        .description("Microservicio de gestión de Diseños")
                        .version("1.0"));
    }
}