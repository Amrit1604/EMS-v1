package com.payroll.salary.client;

import com.payroll.salary.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee-service", url = "${employee-service.url:http://localhost:8081}")
public interface EmployeeServiceClient {
    
    @GetMapping("/api/employees/{id}")
    EmployeeDTO getEmployeeById(@PathVariable("id") String id);
    
    @GetMapping("/api/employees/code/{employeeCode}")
    EmployeeDTO getEmployeeByCode(@PathVariable("employeeCode") String employeeCode);
}
