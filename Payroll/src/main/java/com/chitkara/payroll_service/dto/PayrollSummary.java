package com.chitkara.payroll_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayrollSummary {
    private String month;
    private int totalEmployees;
    private double totalSalaryPaid;
    private double averageSalary;
}