package com.ems.payroll_service.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.math.BigDecimal;

@Document(collection = "employees")
@Data
public class Employee {

    @Id
    private String id;

    private String employeeId; // Unique employee identifier
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String position;
    private LocalDate joinDate;
    private String address;
    private String status; // ACTIVE, INACTIVE, TERMINATED

    // Salary Information
    private BigDecimal baseSalary;
    private BigDecimal allowances;
    private String salaryGrade;
    private String paymentMode; // BANK_TRANSFER, CASH, CHECK
    private String bankAccountNumber;
    private String bankName;

    // Employment Details
    private String employmentType; // FULL_TIME, PART_TIME, CONTRACT
    private String manager;
    private LocalDate contractEndDate;

    // Leave Information
    private Integer annualLeaveBalance;
    private Integer sickLeaveBalance;
    private Integer casualLeaveBalance;
}
