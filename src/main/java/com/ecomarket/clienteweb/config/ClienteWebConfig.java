package com.ecomarket.clienteweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // Indica que esta clase contiene beans de configuración
public class ClienteWebConfig {

    @Bean // Registra un RestTemplate reutilizable en todo el proyecto
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}