package com.ems.payroll_service.controllers;

import com.ems.payroll_service.dto.PayrollDto;
import com.ems.payroll_service.services.PayrollService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PayrollController {

    private final PayrollService payrollService;

    @GetMapping
    public ResponseEntity<List<PayrollDto>> getAllPayrolls() {
        List<PayrollDto> payrolls = payrollService.getAllPayrolls();
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollDto> getPayrollById(@PathVariable String id) {
        return payrollService.getPayrollById(id)
                .map(payroll -> ResponseEntity.ok(payroll))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PayrollDto>> getPayrollsByEmployeeId(@PathVariable String employeeId) {
        List<PayrollDto> payrolls = payrollService.getPayrollsByEmployeeId(employeeId);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/period/{payPeriod}")
    public ResponseEntity<List<PayrollDto>> getPayrollsByPeriod(@PathVariable String payPeriod) {
        List<PayrollDto> payrolls = payrollService.getPayrollsByPeriod(payPeriod);
        return ResponseEntity.ok(payrolls);
    }

    @PostMapping
    public ResponseEntity<PayrollDto> createPayroll(@Valid @RequestBody PayrollDto payrollDto) {
        try {
            PayrollDto createdPayroll = payrollService.createPayroll(payrollDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPayroll);
        } catch (RuntimeException e) {
            log.error("Error creating payroll: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<PayrollDto> generatePayroll(@RequestParam String employeeId,
                                                     @RequestParam String payPeriod) {
        try {
            PayrollDto generatedPayroll = payrollService.generatePayrollForEmployee(employeeId, payPeriod);
            return ResponseEntity.status(HttpStatus.CREATED).body(generatedPayroll);
        } catch (RuntimeException e) {
            log.error("Error generating payroll: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayrollDto> updatePayroll(@PathVariable String id,
                                                   @Valid @RequestBody PayrollDto payrollDto) {
        try {
            PayrollDto updatedPayroll = payrollService.updatePayroll(id, payrollDto);
            return ResponseEntity.ok(updatedPayroll);
        } catch (RuntimeException e) {
            log.error("Error updating payroll: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(@PathVariable String id) {
        try {
            payrollService.deletePayroll(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting payroll: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<PayrollDto> approvePayroll(@PathVariable String id,
                                                    @RequestParam String approvedBy) {
        try {
            PayrollDto approvedPayroll = payrollService.approvePayroll(id, approvedBy);
            return ResponseEntity.ok(approvedPayroll);
        } catch (RuntimeException e) {
            log.error("Error approving payroll: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
