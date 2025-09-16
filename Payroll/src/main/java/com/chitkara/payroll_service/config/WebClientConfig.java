package com.chitkara.payroll_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${employee.service.url}")
    private String employeeServiceUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean("employeeWebClient") // Renamed to avoid conflict
    public WebClient employeeWebClient(WebClient.Builder builder) {
        return builder.baseUrl(employeeServiceUrl).build();
    }
}
