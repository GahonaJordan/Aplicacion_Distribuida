package com.example.ordenes_service.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private final JwtTokenInterceptor jwtTokenInterceptor;

    public RestTemplateConfig(JwtTokenInterceptor jwtTokenInterceptor) {
        this.jwtTokenInterceptor = jwtTokenInterceptor;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Configurar el interceptor para propagar el token JWT
        return builder
                .additionalInterceptors(jwtTokenInterceptor)
                .build();
    }
}
