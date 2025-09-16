package com.chitkara.payroll_service.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeDto {
    private Long id;
    private String name;
    private String email;
    private LocalDate joiningDate;
    private Long departmentId;
}
