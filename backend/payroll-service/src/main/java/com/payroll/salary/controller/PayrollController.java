package com.payroll.salary.controller;

import com.payroll.salary.dto.PayrollDTO;
import com.payroll.salary.entity.Payroll;
import com.payroll.salary.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payroll Management", description = "APIs for managing payrolls and salary processing")
@CrossOrigin(origins = "*")
public class PayrollController {
    
    private final PayrollService payrollService;
    
    @PostMapping
    // @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new payroll", description = "Creates a new payroll record")
    public ResponseEntity<PayrollDTO> createPayroll(@Valid @RequestBody PayrollDTO payrollDTO) {
        log.info("Creating payroll for employee: {}", payrollDTO.getEmployeeCode());
        PayrollDTO createdPayroll = payrollService.createPayroll(payrollDTO);
        return new ResponseEntity<>(createdPayroll, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Update a payroll", description = "Updates an existing payroll record")
    public ResponseEntity<PayrollDTO> updatePayroll(
            @Parameter(description = "Payroll ID") @PathVariable String id,
            @Valid @RequestBody PayrollDTO payrollDTO) {
        log.info("Updating payroll with ID: {}", id);
        PayrollDTO updatedPayroll = payrollService.updatePayroll(id, payrollDTO);
        return ResponseEntity.ok(updatedPayroll);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get payroll by ID", description = "Retrieves a payroll by its ID")
    public ResponseEntity<PayrollDTO> getPayrollById(
            @Parameter(description = "Payroll ID") @PathVariable String id) {
        PayrollDTO payroll = payrollService.getPayrollById(id);
        return ResponseEntity.ok(payroll);
    }
    
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get payrolls by employee ID", description = "Retrieves all payrolls for a specific employee")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByEmployeeId(
            @Parameter(description = "Employee ID") @PathVariable String employeeId) {
        List<PayrollDTO> payrolls = payrollService.getPayrollsByEmployeeId(employeeId);
        return ResponseEntity.ok(payrolls);
    }
    
    @GetMapping("/employee/code/{employeeCode}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get payrolls by employee code", description = "Retrieves all payrolls for a specific employee by code")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByEmployeeCode(
            @Parameter(description = "Employee Code") @PathVariable String employeeCode) {
        List<PayrollDTO> payrolls = payrollService.getPayrollsByEmployeeCode(employeeCode);
        return ResponseEntity.ok(payrolls);
    }
    
    @GetMapping("/employee/{employeeId}/period")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get payroll by employee and period", description = "Retrieves payroll for specific employee and period")
    public ResponseEntity<PayrollDTO> getPayrollByEmployeeAndPeriod(
            @Parameter(description = "Employee ID") @PathVariable String employeeId,
            @Parameter(description = "Month") @RequestParam Integer month,
            @Parameter(description = "Year") @RequestParam Integer year) {
        PayrollDTO payroll = payrollService.getPayrollByEmployeeAndPeriod(employeeId, month, year);
        return ResponseEntity.ok(payroll);
    }
    
    @GetMapping("/period")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get payrolls by period", description = "Retrieves all payrolls for a specific period")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByPeriod(
            @Parameter(description = "Month") @RequestParam Integer month,
            @Parameter(description = "Year") @RequestParam Integer year) {
        List<PayrollDTO> payrolls = payrollService.getPayrollsByPeriod(month, year);
        return ResponseEntity.ok(payrolls);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get payrolls by status", description = "Retrieves all payrolls with a specific status")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByStatus(
            @Parameter(description = "Payment Status") @PathVariable Payroll.PaymentStatus status) {
        List<PayrollDTO> payrolls = payrollService.getPayrollsByStatus(status);
        return ResponseEntity.ok(payrolls);
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get payrolls by date range", description = "Retrieves payrolls within a date range")
    public ResponseEntity<List<PayrollDTO>> getPayrollsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PayrollDTO> payrolls = payrollService.getPayrollsByDateRange(startDate, endDate);
        return ResponseEntity.ok(payrolls);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get all payrolls", description = "Retrieves all payrolls with pagination")
    public ResponseEntity<Page<PayrollDTO>> getAllPayrolls(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PayrollDTO> payrolls = payrollService.getAllPayrolls(pageable);
        return ResponseEntity.ok(payrolls);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Search payrolls", description = "Searches payrolls by employee name or code")
    public ResponseEntity<Page<PayrollDTO>> searchPayrolls(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PayrollDTO> payrolls = payrollService.searchPayrolls(searchTerm, pageable);
        return ResponseEntity.ok(payrolls);
    }
    
    @PostMapping("/generate")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Generate payroll", description = "Generates payroll for a specific employee and period")
    public ResponseEntity<PayrollDTO> generatePayroll(
            @Parameter(description = "Employee ID") @RequestParam String employeeId,
            @Parameter(description = "Month") @RequestParam Integer month,
            @Parameter(description = "Year") @RequestParam Integer year) {
        log.info("Generating payroll for employee: {} for period: {}/{}", employeeId, month, year);
        PayrollDTO generatedPayroll = payrollService.generatePayroll(employeeId, month, year);
        return new ResponseEntity<>(generatedPayroll, HttpStatus.CREATED);
    }
    
    @PostMapping("/generate/bulk")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Generate bulk payroll", description = "Generates payroll for all employees for a specific period")
    public ResponseEntity<List<PayrollDTO>> generateBulkPayroll(
            @Parameter(description = "Month") @RequestParam Integer month,
            @Parameter(description = "Year") @RequestParam Integer year) {
        log.info("Generating bulk payroll for period: {}/{}", month, year);
        List<PayrollDTO> generatedPayrolls = payrollService.generateBulkPayroll(month, year);
        return new ResponseEntity<>(generatedPayrolls, HttpStatus.CREATED);
    }
    
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Approve payroll", description = "Approves a payroll for payment processing")
    public ResponseEntity<PayrollDTO> approvePayroll(
            @Parameter(description = "Payroll ID") @PathVariable String id,
            @Parameter(description = "Approved by") @RequestParam String approvedBy) {
        log.info("Approving payroll with ID: {} by: {}", id, approvedBy);
        PayrollDTO approvedPayroll = payrollService.approvePayroll(id, approvedBy);
        return ResponseEntity.ok(approvedPayroll);
    }
    
    @PatchMapping("/{id}/process-payment")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Process payment", description = "Processes payment for an approved payroll")
    public ResponseEntity<PayrollDTO> processPayment(
            @Parameter(description = "Payroll ID") @PathVariable String id) {
        log.info("Processing payment for payroll ID: {}", id);
        PayrollDTO paidPayroll = payrollService.processPayment(id);
        return ResponseEntity.ok(paidPayroll);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete payroll", description = "Permanently deletes a payroll record")
    public ResponseEntity<Void> deletePayroll(
            @Parameter(description = "Payroll ID") @PathVariable String id) {
        log.info("Deleting payroll with ID: {}", id);
        payrollService.deletePayroll(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/exists")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Check if payroll exists", description = "Checks if payroll exists for employee and period")
    public ResponseEntity<Boolean> existsByEmployeeAndPeriod(
            @Parameter(description = "Employee ID") @RequestParam String employeeId,
            @Parameter(description = "Month") @RequestParam Integer month,
            @Parameter(description = "Year") @RequestParam Integer year) {
        boolean exists = payrollService.existsByEmployeeAndPeriod(employeeId, month, year);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get payroll count by status", description = "Gets the count of payrolls by status")
    public ResponseEntity<Long> getPayrollCountByStatus(
            @Parameter(description = "Payment Status") @PathVariable Payroll.PaymentStatus status) {
        long count = payrollService.getPayrollCountByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/period")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get payroll count by period", description = "Gets the count of payrolls for a period")
    public ResponseEntity<Long> getPayrollCountByPeriod(
            @Parameter(description = "Month") @RequestParam Integer month,
            @Parameter(description = "Year") @RequestParam Integer year) {
        long count = payrollService.getPayrollCountByPeriod(month, year);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/{id}/payslip/pdf")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Generate payslip PDF", description = "Generates and downloads payslip as PDF")
    public ResponseEntity<byte[]> generatePayslipPDF(
            @Parameter(description = "Payroll ID") @PathVariable String id) {
        log.info("Generating payslip PDF for payroll ID: {}", id);
        byte[] pdfBytes = payrollService.generatePayslipPDF(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "payslip_" + id + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
    
    @GetMapping("/report/excel")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Generate payroll report Excel", description = "Generates and downloads payroll report as Excel")
    public ResponseEntity<byte[]> generatePayrollReportExcel(
            @Parameter(description = "Month") @RequestParam Integer month,
            @Parameter(description = "Year") @RequestParam Integer year) {
        log.info("Generating payroll report Excel for period: {}/{}", month, year);
        byte[] excelBytes = payrollService.generatePayrollReportExcel(month, year);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "payroll_report_" + month + "_" + year + ".xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }
}
