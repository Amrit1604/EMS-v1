package com.ems.payroll_service.dto;

import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Data
public class PayrollDto {

    private String id;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    private String employeeName;

    @NotBlank(message = "Pay period is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "Pay period must be in format YYYY-MM")
    private String payPeriod;

    private LocalDate payDate;

    // Earnings
    @DecimalMin(value = "0.0", message = "Base salary must be greater than or equal to 0")
    private BigDecimal baseSalary = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Overtime pay must be greater than or equal to 0")
    private BigDecimal overtimePay = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Bonuses must be greater than or equal to 0")
    private BigDecimal bonuses = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Allowances must be greater than or equal to 0")
    private BigDecimal allowances = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Commissions must be greater than or equal to 0")
    private BigDecimal commissions = BigDecimal.ZERO;

    private BigDecimal totalEarnings;

    // Deductions
    @DecimalMin(value = "0.0", message = "Tax deduction must be greater than or equal to 0")
    private BigDecimal taxDeduction = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Provident fund must be greater than or equal to 0")
    private BigDecimal providentFund = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Insurance must be greater than or equal to 0")
    private BigDecimal insurance = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Loan deduction must be greater than or equal to 0")
    private BigDecimal loanDeduction = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Other deductions must be greater than or equal to 0")
    private BigDecimal otherDeductions = BigDecimal.ZERO;

    private BigDecimal totalDeductions;
    private BigDecimal netPay;

    // Status
    private String status = "DRAFT";
    private String approvedBy;
    private String paymentReference;

    // Working Hours
    @Min(value = 0, message = "Working days cannot be negative")
    @Max(value = 31, message = "Working days cannot exceed 31")
    private Integer workingDays = 0;

    @DecimalMin(value = "0.0", message = "Overtime hours must be greater than or equal to 0")
    private BigDecimal overtimeHours = BigDecimal.ZERO;

    @Min(value = 0, message = "Leave days cannot be negative")
    private Integer leaveDays = 0;
}
