package com.payroll.salary.service.impl;

import com.payroll.salary.entity.Payroll;
import com.payroll.salary.service.TaxCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class TaxCalculationServiceImpl implements TaxCalculationService {
    
    // Tax slabs for Indian Income Tax (2023-24)
    private static final BigDecimal SLAB_1_LIMIT = new BigDecimal("250000");
    private static final BigDecimal SLAB_2_LIMIT = new BigDecimal("500000");
    private static final BigDecimal SLAB_3_LIMIT = new BigDecimal("1000000");
    
    private static final BigDecimal TAX_RATE_SLAB_2 = new BigDecimal("0.05"); // 5%
    private static final BigDecimal TAX_RATE_SLAB_3 = new BigDecimal("0.20"); // 20%
    private static final BigDecimal TAX_RATE_SLAB_4 = new BigDecimal("0.30"); // 30%
    
    // Professional Tax (varies by state, using Maharashtra rates)
    private static final BigDecimal PROFESSIONAL_TAX_MONTHLY = new BigDecimal("200");
    
    @Override
    public Payroll.TaxDetails calculateTax(BigDecimal grossSalary) {
        log.debug("Calculating tax for gross salary: {}", grossSalary);
        
        BigDecimal annualGrossSalary = grossSalary.multiply(new BigDecimal("12"));
        
        // Standard deduction (₹50,000 for FY 2023-24)
        BigDecimal standardDeduction = new BigDecimal("50000");
        BigDecimal taxableIncome = annualGrossSalary.subtract(standardDeduction);
        if (taxableIncome.compareTo(BigDecimal.ZERO) < 0) {
            taxableIncome = BigDecimal.ZERO;
        }
        
        BigDecimal incomeTax = calculateIncomeTax(taxableIncome);
        BigDecimal professionalTax = calculateProfessionalTax(grossSalary);
        
        // Health and Education Cess (4% on income tax)
        BigDecimal cess = incomeTax.multiply(new BigDecimal("0.04"));
        BigDecimal totalTax = incomeTax.add(cess).add(professionalTax);
        
        // Convert annual tax to monthly
        BigDecimal monthlyIncomeTax = incomeTax.add(cess).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        
        String taxSlab = determineTaxSlab(taxableIncome);
        
        return Payroll.TaxDetails.builder()
                .taxableIncome(taxableIncome.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP))
                .incomeTax(monthlyIncomeTax)
                .professionalTax(professionalTax)
                .otherTaxes(BigDecimal.ZERO)
                .totalTax(monthlyIncomeTax.add(professionalTax))
                .taxSlab(taxSlab)
                .build();
    }
    
    @Override
    public BigDecimal calculateIncomeTax(BigDecimal taxableIncome) {
        BigDecimal tax = BigDecimal.ZERO;
        
        if (taxableIncome.compareTo(SLAB_1_LIMIT) <= 0) {
            // No tax for income up to ₹2.5 lakhs
            return tax;
        }
        
        if (taxableIncome.compareTo(SLAB_2_LIMIT) <= 0) {
            // 5% tax on income between ₹2.5 - ₹5 lakhs
            BigDecimal taxableAmount = taxableIncome.subtract(SLAB_1_LIMIT);
            tax = tax.add(taxableAmount.multiply(TAX_RATE_SLAB_2));
        } else if (taxableIncome.compareTo(SLAB_3_LIMIT) <= 0) {
            // 5% on ₹2.5L and 20% on income between ₹5 - ₹10 lakhs
            BigDecimal slab2Amount = SLAB_2_LIMIT.subtract(SLAB_1_LIMIT);
            tax = tax.add(slab2Amount.multiply(TAX_RATE_SLAB_2));
            
            BigDecimal taxableAmount = taxableIncome.subtract(SLAB_2_LIMIT);
            tax = tax.add(taxableAmount.multiply(TAX_RATE_SLAB_3));
        } else {
            // 5% on ₹2.5L, 20% on ₹5L, and 30% on income above ₹10 lakhs
            BigDecimal slab2Amount = SLAB_2_LIMIT.subtract(SLAB_1_LIMIT);
            tax = tax.add(slab2Amount.multiply(TAX_RATE_SLAB_2));
            
            BigDecimal slab3Amount = SLAB_3_LIMIT.subtract(SLAB_2_LIMIT);
            tax = tax.add(slab3Amount.multiply(TAX_RATE_SLAB_3));
            
            BigDecimal taxableAmount = taxableIncome.subtract(SLAB_3_LIMIT);
            tax = tax.add(taxableAmount.multiply(TAX_RATE_SLAB_4));
        }
        
        return tax.setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public BigDecimal calculateProfessionalTax(BigDecimal grossSalary) {
        // Professional tax is typically a fixed monthly amount
        // This varies by state - using Maharashtra rates as example
        if (grossSalary.compareTo(new BigDecimal("21000")) >= 0) {
            return PROFESSIONAL_TAX_MONTHLY;
        } else if (grossSalary.compareTo(new BigDecimal("15000")) >= 0) {
            return new BigDecimal("175");
        } else if (grossSalary.compareTo(new BigDecimal("10000")) >= 0) {
            return new BigDecimal("150");
        } else {
            return BigDecimal.ZERO;
        }
    }
    
    @Override
    public String determineTaxSlab(BigDecimal annualIncome) {
        if (annualIncome.compareTo(SLAB_1_LIMIT) <= 0) {
            return "0% (Up to ₹2.5L)";
        } else if (annualIncome.compareTo(SLAB_2_LIMIT) <= 0) {
            return "5% (₹2.5L - ₹5L)";
        } else if (annualIncome.compareTo(SLAB_3_LIMIT) <= 0) {
            return "20% (₹5L - ₹10L)";
        } else {
            return "30% (Above ₹10L)";
        }
    }
}
