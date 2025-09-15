package com.payroll.salary.service;

import com.payroll.salary.entity.Payroll;

import java.math.BigDecimal;

public interface TaxCalculationService {
    
    Payroll.TaxDetails calculateTax(BigDecimal grossSalary);
    
    BigDecimal calculateIncomeTax(BigDecimal taxableIncome);
    
    BigDecimal calculateProfessionalTax(BigDecimal grossSalary);
    
    String determineTaxSlab(BigDecimal annualIncome);
}
