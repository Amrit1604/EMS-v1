package com.ems.payroll_service.dto;

import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Data
public class EmployeeDto {

    private String id;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Please provide a valid phone number")
    private String phone;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Position is required")
    private String position;

    private LocalDate joinDate;
    private String address;
    private String status = "ACTIVE";

    // Salary Information
    @DecimalMin(value = "0.0", inclusive = false, message = "Base salary must be greater than 0")
    private BigDecimal baseSalary;

    @DecimalMin(value = "0.0", message = "Allowances must be greater than or equal to 0")
    private BigDecimal allowances = BigDecimal.ZERO;

    private String salaryGrade;
    private String paymentMode = "BANK_TRANSFER";
    private String bankAccountNumber;
    private String bankName;

    // Employment Details
    private String employmentType = "FULL_TIME";
    private String manager;
    private LocalDate contractEndDate;

    // Leave Information
    @Min(value = 0, message = "Leave balance cannot be negative")
    private Integer annualLeaveBalance = 21;

    @Min(value = 0, message = "Leave balance cannot be negative")
    private Integer sickLeaveBalance = 10;

    @Min(value = 0, message = "Leave balance cannot be negative")
    private Integer casualLeaveBalance = 5;
}
