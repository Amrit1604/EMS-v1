package com.chitkara.payroll_service.client;

import com.chitkara.payroll_service.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EmployeeServiceClient {

    @Autowired
    @Qualifier("employeeWebClient")
    private WebClient employeeWebClient;

    // ✅ ADD JWT TOKEN PARAMETER
    public EmployeeDto getEmployeeById(Long employeeId, String authHeader) {
        return employeeWebClient
                .get()
                .uri("/api/employees/{id}", employeeId)
                .header("Authorization", authHeader) // ✅ PASS JWT TOKEN
                .retrieve()
                .bodyToMono(EmployeeDto.class)
                .block();
    }
}
