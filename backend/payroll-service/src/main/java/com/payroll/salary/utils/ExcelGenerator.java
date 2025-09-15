package com.payroll.salary.utils;

import com.payroll.salary.entity.Payroll;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class ExcelGenerator {
    
    public byte[] generatePayrollReport(List<Payroll> payrolls) {
        log.info("Generating Excel payroll report for {} payrolls", payrolls.size());
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Payroll Report");
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "Employee Code", "Employee Name", "Month", "Year", "Basic Salary",
                "Gross Salary", "Total Deductions", "Net Salary", "Payment Status",
                "Payment Date", "Transaction Reference"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (Payroll payroll : payrolls) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(payroll.getEmployeeCode());
                row.createCell(1).setCellValue(payroll.getEmployeeName());
                row.createCell(2).setCellValue(payroll.getMonth());
                row.createCell(3).setCellValue(payroll.getYear());
                row.createCell(4).setCellValue(payroll.getBasicSalary().doubleValue());
                row.createCell(5).setCellValue(payroll.getGrossSalary().doubleValue());
                row.createCell(6).setCellValue(payroll.getTotalDeductions().doubleValue());
                row.createCell(7).setCellValue(payroll.getNetSalary().doubleValue());
                row.createCell(8).setCellValue(payroll.getPaymentStatus().toString());
                row.createCell(9).setCellValue(payroll.getPaymentDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                row.createCell(10).setCellValue(payroll.getTransactionReference() != null ? payroll.getTransactionReference() : "");
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            
            log.info("Excel payroll report generated successfully");
            return baos.toByteArray();
            
        } catch (IOException e) {
            log.error("Error generating Excel payroll report", e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }
}
