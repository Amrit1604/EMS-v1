package com.ems.payroll_service.controllers;

import com.ems.payroll_service.services.EmployeeService;
import com.ems.payroll_service.services.PayrollService;
import com.ems.payroll_service.services.AttendanceService;
import com.ems.payroll_service.services.LeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardController {

    private final EmployeeService employeeService;
    private final PayrollService payrollService;
    private final AttendanceService attendanceService;
    private final LeaveService leaveService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Employee Statistics
        summary.put("totalActiveEmployees", employeeService.getEmployeeCountByStatus("ACTIVE"));
        summary.put("totalInactiveEmployees", employeeService.getEmployeeCountByStatus("INACTIVE"));
        summary.put("totalTerminatedEmployees", employeeService.getEmployeeCountByStatus("TERMINATED"));

        // Department wise count
        Map<String, Long> departmentStats = new HashMap<>();
        departmentStats.put("IT", employeeService.getEmployeeCountByDepartment("IT"));
        departmentStats.put("HR", employeeService.getEmployeeCountByDepartment("HR"));
        departmentStats.put("Finance", employeeService.getEmployeeCountByDepartment("Finance"));
        departmentStats.put("Operations", employeeService.getEmployeeCountByDepartment("Operations"));
        departmentStats.put("Marketing", employeeService.getEmployeeCountByDepartment("Marketing"));
        summary.put("departmentStats", departmentStats);

        // Today's attendance summary
        LocalDate today = LocalDate.now();
        summary.put("todayAttendance", attendanceService.getAttendanceByDate(today).size());

        // Pending leaves count
        summary.put("pendingLeaves", leaveService.getLeavesByStatus("PENDING").size());
        summary.put("approvedLeaves", leaveService.getLeavesByStatus("APPROVED").size());

        // Current month payrolls
        String currentMonth = today.getYear() + "-" + String.format("%02d", today.getMonthValue());
        summary.put("currentMonthPayrolls", payrollService.getPayrollsByPeriod(currentMonth).size());

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/employee/{employeeId}/summary")
    public ResponseEntity<Map<String, Object>> getEmployeeSummary(@PathVariable String employeeId) {
        Map<String, Object> summary = new HashMap<>();

        try {
            // Employee details
            employeeService.getEmployeeByEmployeeId(employeeId).ifPresent(employee -> {
                summary.put("employee", employee);
            });

            // This month's attendance
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

            long workingDays = attendanceService.getWorkingDaysForEmployee(employeeId, startOfMonth, endOfMonth);
            double overtimeHours = attendanceService.getOvertimeHoursForEmployee(employeeId, startOfMonth, endOfMonth);

            summary.put("thisMonthWorkingDays", workingDays);
            summary.put("thisMonthOvertimeHours", overtimeHours);

            // Leave balances
            summary.put("annualLeaveBalance", leaveService.getLeaveBalance(employeeId, "ANNUAL"));
            summary.put("sickLeaveBalance", leaveService.getLeaveBalance(employeeId, "SICK"));
            summary.put("casualLeaveBalance", leaveService.getLeaveBalance(employeeId, "CASUAL"));

            // Recent payrolls (last 3 months)
            summary.put("recentPayrolls", payrollService.getPayrollsByEmployeeId(employeeId));

            // Recent leaves
            summary.put("recentLeaves", leaveService.getLeavesByEmployeeId(employeeId));

            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            log.error("Error getting employee summary: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/attendance-report")
    public ResponseEntity<Map<String, Object>> getAttendanceReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<String, Object> report = new HashMap<>();

        // Get all attendance for the period
        var attendanceList = attendanceService.getAllAttendance()
                .stream()
                .filter(att -> !att.getDate().isBefore(startDate) && !att.getDate().isAfter(endDate))
                .toList();

        // Calculate statistics
        long totalPresent = attendanceList.stream()
                .filter(att -> "PRESENT".equals(att.getStatus()))
                .count();

        long totalAbsent = attendanceList.stream()
                .filter(att -> "ABSENT".equals(att.getStatus()))
                .count();

        long totalLate = attendanceList.stream()
                .filter(att -> "LATE".equals(att.getStatus()))
                .count();

        double totalOvertimeHours = attendanceList.stream()
                .mapToDouble(att -> att.getOvertimeHours() != null ? att.getOvertimeHours() : 0.0)
                .sum();

        report.put("totalPresent", totalPresent);
        report.put("totalAbsent", totalAbsent);
        report.put("totalLate", totalLate);
        report.put("totalOvertimeHours", totalOvertimeHours);
        report.put("attendanceData", attendanceList);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/payroll-report")
    public ResponseEntity<Map<String, Object>> getPayrollReport(@RequestParam String payPeriod) {
        Map<String, Object> report = new HashMap<>();

        var payrolls = payrollService.getPayrollsByPeriod(payPeriod);

        // Calculate totals
        double totalEarnings = payrolls.stream()
                .mapToDouble(p -> p.getTotalEarnings() != null ? p.getTotalEarnings().doubleValue() : 0.0)
                .sum();

        double totalDeductions = payrolls.stream()
                .mapToDouble(p -> p.getTotalDeductions() != null ? p.getTotalDeductions().doubleValue() : 0.0)
                .sum();

        double totalNetPay = payrolls.stream()
                .mapToDouble(p -> p.getNetPay() != null ? p.getNetPay().doubleValue() : 0.0)
                .sum();

        long approvedPayrolls = payrolls.stream()
                .filter(p -> "APPROVED".equals(p.getStatus()))
                .count();

        long draftPayrolls = payrolls.stream()
                .filter(p -> "DRAFT".equals(p.getStatus()))
                .count();

        report.put("payPeriod", payPeriod);
        report.put("totalEmployees", payrolls.size());
        report.put("totalEarnings", totalEarnings);
        report.put("totalDeductions", totalDeductions);
        report.put("totalNetPay", totalNetPay);
        report.put("approvedPayrolls", approvedPayrolls);
        report.put("draftPayrolls", draftPayrolls);
        report.put("payrollData", payrolls);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> getServiceHealth() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "payroll-service");
        health.put("timestamp", LocalDate.now().toString());
        return ResponseEntity.ok(health);
    }
}
