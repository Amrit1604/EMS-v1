package com.payroll.salary.dto;

import com.payroll.salary.entity.Payroll;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollDTO {
    
    private String id;
    
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
    
    @NotBlank(message = "Employee code is required")
    private String employeeCode;
    
    @NotBlank(message = "Employee name is required")
    private String employeeName;
    
    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;
    
    @NotNull(message = "Year is required")
    @Min(value = 2020, message = "Year must be 2020 or later")
    private Integer year;
    
    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;
    
    @NotNull(message = "Basic salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Basic salary must be greater than 0")
    private BigDecimal basicSalary;
    
    private List<AllowanceDTO> allowances;
    
    private List<DeductionDTO> deductions;
    
    private BigDecimal grossSalary;
    
    private BigDecimal totalDeductions;
    
    private BigDecimal netSalary;
    
    private TaxDetailsDTO taxDetails;
    
    @NotNull(message = "Payment status is required")
    private Payroll.PaymentStatus paymentStatus;
    
    @NotNull(message = "Payment method is required")
    private Payroll.PaymentMethod paymentMethod;
    
    private String transactionReference;
    
    private String bankAccountNumber;
    
    private String remarks;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private String createdBy;
    
    private String approvedBy;
    
    private LocalDateTime approvedDate;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AllowanceDTO {
        @NotBlank(message = "Allowance name is required")
        private String name;
        
        @NotNull(message = "Allowance amount is required")
        @DecimalMin(value = "0.0", message = "Allowance amount must be non-negative")
        private BigDecimal amount;
        
        @NotNull(message = "Allowance type is required")
        private Payroll.AllowanceType type;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeductionDTO {
        @NotBlank(message = "Deduction name is required")
        private String name;
        
        @NotNull(message = "Deduction amount is required")
        @DecimalMin(value = "0.0", message = "Deduction amount must be non-negative")
        private BigDecimal amount;
        
        @NotNull(message = "Deduction type is required")
        private Payroll.DeductionType type;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TaxDetailsDTO {
        private BigDecimal taxableIncome;
        private BigDecimal incomeTax;
        private BigDecimal professionalTax;
        private BigDecimal otherTaxes;
        private BigDecimal totalTax;
        private String taxSlab;
    }
}
