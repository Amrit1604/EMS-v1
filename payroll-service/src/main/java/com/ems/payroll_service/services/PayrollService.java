package com.ems.payroll_service.services;

import com.ems.payroll_service.dto.PayrollDto;
import com.ems.payroll_service.models.Employee;
import com.ems.payroll_service.models.Payroll;
import com.ems.payroll_service.repositories.EmployeeRepository;
import com.ems.payroll_service.repositories.PayrollRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceService attendanceService;

    public List<PayrollDto> getAllPayrolls() {
        log.info("Fetching all payrolls");
        return payrollRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PayrollDto> getPayrollById(String id) {
        log.info("Fetching payroll by ID: {}", id);
        return payrollRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<PayrollDto> getPayrollsByEmployeeId(String employeeId) {
        log.info("Fetching payrolls for employee: {}", employeeId);
        return payrollRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PayrollDto> getPayrollsByPeriod(String payPeriod) {
        log.info("Fetching payrolls for period: {}", payPeriod);
        return payrollRepository.findByPayPeriod(payPeriod)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PayrollDto createPayroll(PayrollDto payrollDto) {
        log.info("Creating payroll for employee: {} for period: {}",
                payrollDto.getEmployeeId(), payrollDto.getPayPeriod());

        // Check if employee exists
        Employee employee = employeeRepository.findByEmployeeId(payrollDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + payrollDto.getEmployeeId()));

        // Check if payroll already exists for this employee and period
        Optional<Payroll> existingPayroll = payrollRepository
                .findByEmployeeIdAndPayPeriod(payrollDto.getEmployeeId(), payrollDto.getPayPeriod());

        if (existingPayroll.isPresent()) {
            throw new RuntimeException("Payroll already exists for employee " +
                    payrollDto.getEmployeeId() + " for period " + payrollDto.getPayPeriod());
        }

        Payroll payroll = convertToEntity(payrollDto);
        payroll.setEmployeeName(employee.getFullName());
        payroll.setCreatedAt(LocalDateTime.now());

        // Calculate payroll automatically if not provided
        if (payroll.getBaseSalary() == null || payroll.getBaseSalary().equals(BigDecimal.ZERO)) {
            calculatePayroll(payroll, employee);
        } else {
            calculateTotals(payroll);
        }

        Payroll savedPayroll = payrollRepository.save(payroll);
        log.info("Payroll created successfully with ID: {}", savedPayroll.getId());

        return convertToDto(savedPayroll);
    }

    public PayrollDto updatePayroll(String id, PayrollDto payrollDto) {
        log.info("Updating payroll with ID: {}", id);

        Payroll existingPayroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));

        // Don't allow update if already approved
        if ("APPROVED".equals(existingPayroll.getStatus()) || "PAID".equals(existingPayroll.getStatus())) {
            throw new RuntimeException("Cannot update payroll that is already approved or paid");
        }

        BeanUtils.copyProperties(payrollDto, existingPayroll, "id", "createdAt", "employeeName");
        existingPayroll.setUpdatedAt(LocalDateTime.now());

        calculateTotals(existingPayroll);

        Payroll updatedPayroll = payrollRepository.save(existingPayroll);
        log.info("Payroll updated successfully: {}", updatedPayroll.getId());

        return convertToDto(updatedPayroll);
    }

    public void deletePayroll(String id) {
        log.info("Deleting payroll with ID: {}", id);

        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));

        if ("APPROVED".equals(payroll.getStatus()) || "PAID".equals(payroll.getStatus())) {
            throw new RuntimeException("Cannot delete payroll that is already approved or paid");
        }

        payrollRepository.deleteById(id);
        log.info("Payroll deleted successfully: {}", id);
    }

    public PayrollDto approvePayroll(String id, String approvedBy) {
        log.info("Approving payroll with ID: {} by: {}", id, approvedBy);

        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with ID: " + id));

        if (!"DRAFT".equals(payroll.getStatus())) {
            throw new RuntimeException("Only draft payrolls can be approved");
        }

        payroll.setStatus("APPROVED");
        payroll.setApprovedBy(approvedBy);
        payroll.setApprovedAt(LocalDateTime.now());
        payroll.setUpdatedAt(LocalDateTime.now());

        Payroll approvedPayroll = payrollRepository.save(payroll);
        log.info("Payroll approved successfully: {}", approvedPayroll.getId());

        return convertToDto(approvedPayroll);
    }

    public PayrollDto generatePayrollForEmployee(String employeeId, String payPeriod) {
        log.info("Generating payroll for employee: {} for period: {}", employeeId, payPeriod);

        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        // Check if payroll already exists
        Optional<Payroll> existingPayroll = payrollRepository
                .findByEmployeeIdAndPayPeriod(employeeId, payPeriod);

        if (existingPayroll.isPresent()) {
            throw new RuntimeException("Payroll already exists for employee " + employeeId +
                    " for period " + payPeriod);
        }

        Payroll payroll = new Payroll();
        payroll.setEmployeeId(employeeId);
        payroll.setEmployeeName(employee.getFullName());
        payroll.setPayPeriod(payPeriod);
        payroll.setCreatedAt(LocalDateTime.now());
        payroll.setStatus("DRAFT");

        calculatePayroll(payroll, employee);

        Payroll savedPayroll = payrollRepository.save(payroll);
        return convertToDto(savedPayroll);
    }

    private void calculatePayroll(Payroll payroll, Employee employee) {
        // Set base salary from employee record
        payroll.setBaseSalary(employee.getBaseSalary());
        payroll.setAllowances(employee.getAllowances() != null ? employee.getAllowances() : BigDecimal.ZERO);

        // Calculate working days and overtime for the period
        String[] periodParts = payroll.getPayPeriod().split("-");
        int year = Integer.parseInt(periodParts[0]);
        int month = Integer.parseInt(periodParts[1]);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Get attendance data for the period
        long presentDays = attendanceService.getWorkingDaysForEmployee(
                payroll.getEmployeeId(), startDate, endDate);
        double overtimeHours = attendanceService.getOvertimeHoursForEmployee(
                payroll.getEmployeeId(), startDate, endDate);

        payroll.setWorkingDays((int) presentDays);
        payroll.setOvertimeHours(BigDecimal.valueOf(overtimeHours));

        // Calculate overtime pay (assuming 1.5x hourly rate)
        BigDecimal dailyRate = employee.getBaseSalary().divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);
        BigDecimal hourlyRate = dailyRate.divide(BigDecimal.valueOf(8), 2, RoundingMode.HALF_UP);
        BigDecimal overtimeRate = hourlyRate.multiply(BigDecimal.valueOf(1.5));

        payroll.setOvertimePay(overtimeRate.multiply(BigDecimal.valueOf(overtimeHours)));

        // Set default deductions (can be customized based on business rules)
        calculateDefaultDeductions(payroll);

        // Calculate totals
        calculateTotals(payroll);
    }

    private void calculateDefaultDeductions(Payroll payroll) {
        BigDecimal grossPay = payroll.getBaseSalary()
                .add(payroll.getOvertimePay() != null ? payroll.getOvertimePay() : BigDecimal.ZERO)
                .add(payroll.getAllowances() != null ? payroll.getAllowances() : BigDecimal.ZERO)
                .add(payroll.getBonuses() != null ? payroll.getBonuses() : BigDecimal.ZERO)
                .add(payroll.getCommissions() != null ? payroll.getCommissions() : BigDecimal.ZERO);

        // Tax calculation (simplified - 10% of gross pay)
        payroll.setTaxDeduction(grossPay.multiply(BigDecimal.valueOf(0.10)));

        // Provident Fund (12% of basic salary)
        payroll.setProvidentFund(payroll.getBaseSalary().multiply(BigDecimal.valueOf(0.12)));

        // Insurance (fixed amount)
        payroll.setInsurance(BigDecimal.valueOf(500));

        // Set defaults for other fields if null
        if (payroll.getLoanDeduction() == null) payroll.setLoanDeduction(BigDecimal.ZERO);
        if (payroll.getOtherDeductions() == null) payroll.setOtherDeductions(BigDecimal.ZERO);
    }

    private void calculateTotals(Payroll payroll) {
        // Calculate total earnings
        BigDecimal totalEarnings = BigDecimal.ZERO
                .add(payroll.getBaseSalary() != null ? payroll.getBaseSalary() : BigDecimal.ZERO)
                .add(payroll.getOvertimePay() != null ? payroll.getOvertimePay() : BigDecimal.ZERO)
                .add(payroll.getBonuses() != null ? payroll.getBonuses() : BigDecimal.ZERO)
                .add(payroll.getAllowances() != null ? payroll.getAllowances() : BigDecimal.ZERO)
                .add(payroll.getCommissions() != null ? payroll.getCommissions() : BigDecimal.ZERO);

        payroll.setTotalEarnings(totalEarnings);

        // Calculate total deductions
        BigDecimal totalDeductions = BigDecimal.ZERO
                .add(payroll.getTaxDeduction() != null ? payroll.getTaxDeduction() : BigDecimal.ZERO)
                .add(payroll.getProvidentFund() != null ? payroll.getProvidentFund() : BigDecimal.ZERO)
                .add(payroll.getInsurance() != null ? payroll.getInsurance() : BigDecimal.ZERO)
                .add(payroll.getLoanDeduction() != null ? payroll.getLoanDeduction() : BigDecimal.ZERO)
                .add(payroll.getOtherDeductions() != null ? payroll.getOtherDeductions() : BigDecimal.ZERO);

        payroll.setTotalDeductions(totalDeductions);

        // Calculate net pay
        payroll.setNetPay(totalEarnings.subtract(totalDeductions));
    }

    private PayrollDto convertToDto(Payroll payroll) {
        PayrollDto dto = new PayrollDto();
        BeanUtils.copyProperties(payroll, dto);
        return dto;
    }

    private Payroll convertToEntity(PayrollDto dto) {
        Payroll payroll = new Payroll();
        BeanUtils.copyProperties(dto, payroll, "id");
        return payroll;
    }
}
