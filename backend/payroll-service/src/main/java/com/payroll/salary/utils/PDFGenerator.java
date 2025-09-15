package com.payroll.salary.utils;

import com.payroll.salary.entity.Payroll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class PDFGenerator {
    
    public byte[] generatePayslip(Payroll payroll) {
        log.info("Generating PDF payslip for payroll ID: {}", payroll.getId());
        
        try {
            // This is a simplified implementation
            // In a real application, you would use iText7 or similar library
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            String payslipContent = generatePayslipContent(payroll);
            baos.write(payslipContent.getBytes());
            
            log.info("PDF payslip generated successfully for payroll ID: {}", payroll.getId());
            return baos.toByteArray();
            
        } catch (Exception e) {
            log.error("Error generating PDF payslip for payroll ID: {}", payroll.getId(), e);
            throw new RuntimeException("Failed to generate PDF payslip", e);
        }
    }
    
    private String generatePayslipContent(Payroll payroll) {
        StringBuilder content = new StringBuilder();
        
        content.append("PAYSLIP\n");
        content.append("=======\n\n");
        content.append("Employee: ").append(payroll.getEmployeeName()).append("\n");
        content.append("Employee Code: ").append(payroll.getEmployeeCode()).append("\n");
        content.append("Period: ").append(payroll.getMonth()).append("/").append(payroll.getYear()).append("\n");
        content.append("Payment Date: ").append(payroll.getPaymentDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("\n\n");
        
        content.append("EARNINGS:\n");
        content.append("---------\n");
        content.append("Basic Salary: ₹").append(payroll.getBasicSalary()).append("\n");
        
        if (payroll.getAllowances() != null) {
            for (Payroll.Allowance allowance : payroll.getAllowances()) {
                content.append(allowance.getName()).append(": ₹").append(allowance.getAmount()).append("\n");
            }
        }
        
        content.append("Gross Salary: ₹").append(payroll.getGrossSalary()).append("\n\n");
        
        content.append("DEDUCTIONS:\n");
        content.append("-----------\n");
        
        if (payroll.getDeductions() != null) {
            for (Payroll.Deduction deduction : payroll.getDeductions()) {
                content.append(deduction.getName()).append(": ₹").append(deduction.getAmount()).append("\n");
            }
        }
        
        if (payroll.getTaxDetails() != null) {
            content.append("Income Tax: ₹").append(payroll.getTaxDetails().getIncomeTax()).append("\n");
            content.append("Professional Tax: ₹").append(payroll.getTaxDetails().getProfessionalTax()).append("\n");
        }
        
        content.append("Total Deductions: ₹").append(payroll.getTotalDeductions()).append("\n\n");
        
        content.append("NET SALARY: ₹").append(payroll.getNetSalary()).append("\n");
        
        return content.toString();
    }
}
