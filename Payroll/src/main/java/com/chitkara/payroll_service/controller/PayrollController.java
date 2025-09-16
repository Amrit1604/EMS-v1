package com.chitkara.payroll_service.controller;

import com.chitkara.payroll_service.dto.PayrollSummary;
import com.chitkara.payroll_service.entity.PayrollRun;
import com.chitkara.payroll_service.service.PayrollService;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest; // ✅ ADD THIS IMPORT

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService){
        this.payrollService = payrollService;
    }

    @PostMapping("/generate/{empId}")
    public PayrollRun generatePayroll(@PathVariable Long empId,
                                      @RequestParam String month,
                                      HttpServletRequest request) { // ✅ ADD REQUEST PARAMETER

        // ✅ EXTRACT JWT TOKEN FROM HEADER
        String authHeader = request.getHeader("Authorization");

        return payrollService.generatePayroll(empId, LocalDate.parse(month + "-01"), authHeader);
    }

    @GetMapping("/{empId}")
    public List<PayrollRun> getPayroll(@PathVariable Long empId){
        return payrollService.getPayrollByEmployee(empId);
    }

    @GetMapping("/report/{month}")
    public PayrollSummary getMonthlyReport(@PathVariable String month) {
        LocalDate monthDate = LocalDate.parse(month + "-01");
        List<PayrollRun> runs = payrollService.getPayrollByMonth(monthDate);

        double totalSalary = runs.stream()
                .mapToDouble(PayrollRun::getTotalSalary)
                .sum();

        return PayrollSummary.builder()
                .month(month)
                .totalEmployees(runs.size())
                .totalSalaryPaid(totalSalary)
                .averageSalary(runs.isEmpty() ? 0 : totalSalary / runs.size())
                .build();
    }
}
