package com.dreamysips.service_cliente.config;

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
                         .title("DreamySips - Cliente API")
                        .description("Microservicio de gestión de Cliente")
                        .version("1.0"));
    }
}