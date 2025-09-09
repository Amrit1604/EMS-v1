package com.ems.payroll_service.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Document(collection = "payrolls")
@Data
public class Payroll {

    @Id
    private String id;

    private String employeeId;
    private String employeeName;
    private String payPeriod; // "2024-01", "2024-02" etc.
    private LocalDate payDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Earnings
    private BigDecimal baseSalary;
    private BigDecimal overtimePay;
    private BigDecimal bonuses;
    private BigDecimal allowances;
    private BigDecimal commissions;
    private BigDecimal totalEarnings;

    // Deductions
    private BigDecimal taxDeduction;
    private BigDecimal providentFund;
    private BigDecimal insurance;
    private BigDecimal loanDeduction;
    private BigDecimal otherDeductions;
    private BigDecimal totalDeductions;

    // Net Pay
    private BigDecimal netPay;

    // Status
    private String status; // DRAFT, APPROVED, PAID, CANCELLED
    private String approvedBy;
    private LocalDateTime approvedAt;
    private String paymentReference;

    // Working Hours
    private Integer workingDays;
    private BigDecimal overtimeHours;
    private Integer leaveDays;
}
