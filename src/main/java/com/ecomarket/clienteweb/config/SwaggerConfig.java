package com.ecomarket.clienteweb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI clienteWebOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cliente Web API")
                        .description("Microservicio Cliente Web de EcoMarket SPA")
                        .version("v1.0"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("cliente-web")
                .packagesToScan("com.ecomarket.clienteweb.controller")
                .build();
    }
}