package com.chitkara.payroll_service.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chitkara.payroll_service.client.EmployeeServiceClient;
import com.chitkara.payroll_service.dto.EmployeeDto;
import com.chitkara.payroll_service.entity.EmployeeSalaryComponent;
import com.chitkara.payroll_service.entity.PayrollRun;
import com.chitkara.payroll_service.entity.SalaryComponent;
import com.chitkara.payroll_service.repository.EmployeeSalaryComponentRepository;
import com.chitkara.payroll_service.repository.PayrollRunRepository;
import com.chitkara.payroll_service.repository.SalaryComponentRepository;

@Service
public class PayrollService {

    private final PayrollRunRepository payrollRunRepository;
    private final EmployeeSalaryComponentRepository empSalaryCompRepo;
    private final SalaryComponentRepository salaryComponentRepo;
    private final EmployeeServiceClient employeeServiceClient;

    public PayrollService(PayrollRunRepository payrollRunRepository,
                          EmployeeSalaryComponentRepository empSalaryCompRepo,
                          SalaryComponentRepository salaryComponentRepo,
                          EmployeeServiceClient employeeServiceClient) {
        this.payrollRunRepository = payrollRunRepository;
        this.empSalaryCompRepo = empSalaryCompRepo;
        this.salaryComponentRepo = salaryComponentRepo;
        this.employeeServiceClient = employeeServiceClient;
    }

    // ---------------- Generate payroll for an employee ----------------
    public PayrollRun generatePayroll(Long employeeId, LocalDate month, String authHeader) {

        // Fetch employee details using EmployeeServiceClient
        EmployeeDto employee = employeeServiceClient.getEmployeeById(employeeId, authHeader);
        if (employee == null) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        // Fetch assigned salary components
        List<EmployeeSalaryComponent> components = empSalaryCompRepo.findByEmployeeId(employeeId);

        // If no components assigned, assign default components (from SalaryComponent table)
        if (components.isEmpty()) {
            List<SalaryComponent> defaults = salaryComponentRepo.findAll();
            for (SalaryComponent sc : defaults) {
                EmployeeSalaryComponent esc = EmployeeSalaryComponent.builder()
                        .employeeId(employeeId)
                        .salaryComponent(sc)
                        .amount(sc.getAmount())
                        .build();
                empSalaryCompRepo.save(esc);
                components.add(esc);
            }
        }

        // Calculate total salary
        double totalSalary = components.stream()
                .mapToDouble(EmployeeSalaryComponent::getAmount)
                .sum();

        // Build breakdown JSON manually
        String breakdownJson = "{}";
        if (!components.isEmpty()) {
            StringBuilder sb = new StringBuilder("{");
            for (int i = 0; i < components.size(); i++) {
                EmployeeSalaryComponent comp = components.get(i);
                sb.append("\"").append(comp.getSalaryComponent().getName())
                  .append("\":").append(comp.getAmount());
                if (i < components.size() - 1) sb.append(",");
            }
            sb.append("}");
            breakdownJson = sb.toString();
        }

        // Save payroll run
        PayrollRun payroll = PayrollRun.builder()
                .employeeId(employee.getId())
                .employeeName(employee.getName())
                .month(month)
                .totalSalary(totalSalary)
                .salaryBreakdown(breakdownJson)
                .build();

        return payrollRunRepository.save(payroll);
    }

    // ---------------- Get payrolls by employee ----------------
    public List<PayrollRun> getPayrollByEmployee(Long employeeId) {
        return payrollRunRepository.findByEmployeeId(employeeId);
    }

    // ---------------- Get payrolls by month ----------------
    public List<PayrollRun> getPayrollByMonth(LocalDate month) {
        return payrollRunRepository.findByMonth(month);
    }
}
