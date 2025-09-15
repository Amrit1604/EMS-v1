package com.payroll.salary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "payrolls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {
    
    @Id
    private String id;
    
    @Indexed
    private String employeeId;
    
    private String employeeCode;
    
    private String employeeName;
    
    private Integer month;
    
    private Integer year;
    
    @Indexed
    private LocalDate paymentDate;
    
    private BigDecimal basicSalary;
    
    private List<Allowance> allowances;
    
    private List<Deduction> deductions;
    
    private BigDecimal grossSalary;
    
    private BigDecimal totalDeductions;
    
    private BigDecimal netSalary;
    
    private TaxDetails taxDetails;
    
    private PaymentStatus paymentStatus;
    
    private PaymentMethod paymentMethod;
    
    private String transactionReference;
    
    private String bankAccountNumber;
    
    private String remarks;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private String createdBy;
    
    private String approvedBy;
    
    private LocalDateTime approvedDate;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Allowance {
        private String name;
        private BigDecimal amount;
        private AllowanceType type;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Deduction {
        private String name;
        private BigDecimal amount;
        private DeductionType type;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TaxDetails {
        private BigDecimal taxableIncome;
        private BigDecimal incomeTax;
        private BigDecimal professionalTax;
        private BigDecimal otherTaxes;
        private BigDecimal totalTax;
        private String taxSlab;
    }
    
    public enum PaymentStatus {
        DRAFT, PENDING_APPROVAL, APPROVED, PAID, CANCELLED, FAILED
    }
    
    public enum PaymentMethod {
        BANK_TRANSFER, CHEQUE, CASH, DIGITAL_WALLET
    }
    
    public enum AllowanceType {
        HRA, DA, TA, MEDICAL, SPECIAL, BONUS, OVERTIME, OTHER
    }
    
    public enum DeductionType {
        PF, ESI, INCOME_TAX, PROFESSIONAL_TAX, LOAN, ADVANCE, OTHER
    }
}