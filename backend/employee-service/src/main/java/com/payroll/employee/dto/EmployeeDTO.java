package com.payroll.employee.dto;

import com.payroll.employee.entity.Address;
import com.payroll.employee.entity.BankDetails;
import com.payroll.employee.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {
    
    private String id;
    
    @NotBlank(message = "Employee code is required")
    private String employeeCode;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number should be valid")
    private String phoneNumber;
    
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;
    
    @NotBlank(message = "Department ID is required")
    private String departmentId;
    
    private String departmentName;
    
    @NotBlank(message = "Designation ID is required")
    private String designationId;
    
    private String designationTitle;
    
    @NotNull(message = "Base salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base salary must be greater than 0")
    private BigDecimal baseSalary;
    
    @NotNull(message = "Employee status is required")
    private Employee.EmployeeStatus status;
    
    @NotNull(message = "Employment type is required")
    private Employee.EmploymentType employmentType;
    
    private String managerId;
    
    private String managerName;
    
    private Address address;
    
    private BankDetails bankDetails;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private String createdBy;
    
    private String updatedBy;
}
