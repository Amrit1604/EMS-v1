package com.payroll.salary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    
    private String id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private LocalDate joiningDate;
    private String departmentId;
    private String departmentName;
    private String designationId;
    private String designationTitle;
    private BigDecimal baseSalary;
    private String status;
    private String employmentType;
    private String managerId;
    private String managerName;
}
