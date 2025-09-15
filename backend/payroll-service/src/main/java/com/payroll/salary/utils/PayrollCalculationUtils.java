package com.payroll.salary.utils;

import com.payroll.salary.entity.Payroll;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class PayrollCalculationUtils {
    
    public List<Payroll.Allowance> generateStandardAllowances(BigDecimal baseSalary) {
        List<Payroll.Allowance> allowances = new ArrayList<>();
        
        // HRA - 40% of basic salary (for metro cities)
        BigDecimal hra = baseSalary.multiply(new BigDecimal("0.40")).setScale(2, RoundingMode.HALF_UP);
        allowances.add(Payroll.Allowance.builder()
                .name("House Rent Allowance")
                .amount(hra)
                .type(Payroll.AllowanceType.HRA)
                .build());
        
        // DA - 10% of basic salary
        BigDecimal da = baseSalary.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        allowances.add(Payroll.Allowance.builder()
                .name("Dearness Allowance")
                .amount(da)
                .type(Payroll.AllowanceType.DA)
                .build());
        
        // Medical Allowance - Fixed amount
        allowances.add(Payroll.Allowance.builder()
                .name("Medical Allowance")
                .amount(new BigDecimal("1250"))
                .type(Payroll.AllowanceType.MEDICAL)
                .build());
        
        // Transport Allowance - Fixed amount
        allowances.add(Payroll.Allowance.builder()
                .name("Transport Allowance")
                .amount(new BigDecimal("1600"))
                .type(Payroll.AllowanceType.TA)
                .build());
        
        return allowances;
    }
    
    public List<Payroll.Deduction> generateStandardDeductions(BigDecimal baseSalary) {
        List<Payroll.Deduction> deductions = new ArrayList<>();
        
        // PF - 12% of basic salary (employee contribution)
        BigDecimal pf = baseSalary.multiply(new BigDecimal("0.12")).setScale(2, RoundingMode.HALF_UP);
        deductions.add(Payroll.Deduction.builder()
                .name("Provident Fund")
                .amount(pf)
                .type(Payroll.DeductionType.PF)
                .build());
        
        // ESI - 0.75% of gross salary (if gross salary <= ₹21,000)
        BigDecimal grossSalary = baseSalary.multiply(new BigDecimal("1.50")); // Approximate gross
        if (grossSalary.compareTo(new BigDecimal("21000")) <= 0) {
            BigDecimal esi = grossSalary.multiply(new BigDecimal("0.0075")).setScale(2, RoundingMode.HALF_UP);
            deductions.add(Payroll.Deduction.builder()
                    .name("Employee State Insurance")
                    .amount(esi)
                    .type(Payroll.DeductionType.ESI)
                    .build());
        }
        
        return deductions;
    }
    
    public BigDecimal calculateGrossSalary(BigDecimal basicSalary, List<Payroll.Allowance> allowances) {
        BigDecimal gross = basicSalary;
        if (allowances != null) {
            for (Payroll.Allowance allowance : allowances) {
                gross = gross.add(allowance.getAmount());
            }
        }
        return gross.setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal calculateTotalDeductions(List<Payroll.Deduction> deductions) {
        BigDecimal total = BigDecimal.ZERO;
        if (deductions != null) {
            for (Payroll.Deduction deduction : deductions) {
                total = total.add(deduction.getAmount());
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal calculateNetSalary(BigDecimal grossSalary, BigDecimal totalDeductions) {
        return grossSalary.subtract(totalDeductions).setScale(2, RoundingMode.HALF_UP);
    }
}
