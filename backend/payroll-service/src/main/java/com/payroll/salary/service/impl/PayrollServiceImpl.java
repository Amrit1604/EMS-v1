package com.payroll.salary.service.impl;

import com.payroll.salary.client.EmployeeServiceClient;
import com.payroll.salary.dto.EmployeeDTO;
import com.payroll.salary.dto.PayrollDTO;
import com.payroll.salary.entity.Payroll;
import com.payroll.salary.exception.DuplicateResourceException;
import com.payroll.salary.exception.ResourceNotFoundException;
import com.payroll.salary.repository.PayrollRepository;
import com.payroll.salary.service.PayrollService;
import com.payroll.salary.service.TaxCalculationService;
import com.payroll.salary.utils.PayrollCalculationUtils;
import com.payroll.salary.utils.PDFGenerator;
import com.payroll.salary.utils.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PayrollServiceImpl implements PayrollService {
    
    private final PayrollRepository payrollRepository;
    private final EmployeeServiceClient employeeServiceClient;
    private final TaxCalculationService taxCalculationService;
    private final PayrollCalculationUtils calculationUtils;
    private final PDFGenerator pdfGenerator;
    private final ExcelGenerator excelGenerator;
    
    @Override
    public PayrollDTO createPayroll(PayrollDTO payrollDTO) {
        log.info("Creating payroll for employee: {} for period: {}/{}", 
                payrollDTO.getEmployeeCode(), payrollDTO.getMonth(), payrollDTO.getYear());
        
        if (payrollRepository.existsByEmployeeIdAndMonthAndYear(
                payrollDTO.getEmployeeId(), payrollDTO.getMonth(), payrollDTO.getYear())) {
            throw new DuplicateResourceException(
                    String.format("Payroll already exists for employee %s for period %d/%d", 
                            payrollDTO.getEmployeeCode(), payrollDTO.getMonth(), payrollDTO.getYear()));
        }
        
        Payroll payroll = mapToEntity(payrollDTO);
        payroll.setCreatedAt(LocalDateTime.now());
        payroll.setUpdatedAt(LocalDateTime.now());
        
        // Simple calculation without complex dependencies for testing
        if (payroll.getGrossSalary() == null) {
            payroll.setGrossSalary(payroll.getBasicSalary());
        }
        if (payroll.getTotalDeductions() == null) {
            payroll.setTotalDeductions(BigDecimal.ZERO);
        }
        if (payroll.getNetSalary() == null) {
            payroll.setNetSalary(payroll.getGrossSalary().subtract(payroll.getTotalDeductions()));
        }
        
        Payroll savedPayroll = payrollRepository.save(payroll);
        log.info("Payroll created successfully with ID: {}", savedPayroll.getId());
        
        return mapToDTO(savedPayroll);
    }
    
    @Override
    public PayrollDTO updatePayroll(String id, PayrollDTO payrollDTO) {
        log.info("Updating payroll with ID: {}", id);
        
        Payroll existingPayroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with ID: " + id));
        
        updatePayrollFields(existingPayroll, payrollDTO);
        existingPayroll.setUpdatedAt(LocalDateTime.now());
        
        // Recalculate payroll amounts
        calculatePayrollAmounts(existingPayroll);
        
        Payroll updatedPayroll = payrollRepository.save(existingPayroll);
        log.info("Payroll updated successfully with ID: {}", updatedPayroll.getId());
        
        return mapToDTO(updatedPayroll);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PayrollDTO getPayrollById(String id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with ID: " + id));
        return mapToDTO(payroll);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PayrollDTO> getPayrollsByEmployeeId(String employeeId) {
        return payrollRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PayrollDTO> getPayrollsByEmployeeCode(String employeeCode) {
        return payrollRepository.findByEmployeeCode(employeeCode)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PayrollDTO getPayrollByEmployeeAndPeriod(String employeeId, Integer month, Integer year) {
        Payroll payroll = payrollRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Payroll not found for employee %s for period %d/%d", employeeId, month, year)));
        return mapToDTO(payroll);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PayrollDTO> getPayrollsByPeriod(Integer month, Integer year) {
        return payrollRepository.findByMonthAndYear(month, year)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PayrollDTO> getPayrollsByStatus(Payroll.PaymentStatus paymentStatus) {
        return payrollRepository.findByPaymentStatus(paymentStatus)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PayrollDTO> getPayrollsByDateRange(LocalDate startDate, LocalDate endDate) {
        return payrollRepository.findByPaymentDateBetween(startDate, endDate)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PayrollDTO> getAllPayrolls(Pageable pageable) {
        return payrollRepository.findAll(pageable).map(this::mapToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<PayrollDTO> searchPayrolls(String searchTerm, Pageable pageable) {
        return payrollRepository.findBySearchTerm(searchTerm, pageable).map(this::mapToDTO);
    }
    
    @Override
    public PayrollDTO generatePayroll(String employeeId, Integer month, Integer year) {
        log.info("Generating payroll for employee: {} for period: {}/{}", employeeId, month, year);
        
        if (payrollRepository.existsByEmployeeIdAndMonthAndYear(employeeId, month, year)) {
            throw new DuplicateResourceException(
                    String.format("Payroll already exists for employee %s for period %d/%d", employeeId, month, year));
        }
        
        // Fetch employee details
        EmployeeDTO employee = employeeServiceClient.getEmployeeById(employeeId);
        
        // Create payroll
        Payroll payroll = Payroll.builder()
                .employeeId(employeeId)
                .employeeCode(employee.getEmployeeCode())
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .month(month)
                .year(year)
                .paymentDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()))
                .basicSalary(employee.getBaseSalary())
                .paymentStatus(Payroll.PaymentStatus.DRAFT)
                .paymentMethod(Payroll.PaymentMethod.BANK_TRANSFER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        // Generate allowances and deductions
        payroll.setAllowances(calculationUtils.generateStandardAllowances(employee.getBaseSalary()));
        payroll.setDeductions(calculationUtils.generateStandardDeductions(employee.getBaseSalary()));
        
        // Calculate amounts
        calculatePayrollAmounts(payroll);
        
        Payroll savedPayroll = payrollRepository.save(payroll);
        log.info("Payroll generated successfully with ID: {}", savedPayroll.getId());
        
        return mapToDTO(savedPayroll);
    }
    
    @Override
    public PayrollDTO approvePayroll(String id, String approvedBy) {
        log.info("Approving payroll with ID: {} by: {}", id, approvedBy);
        
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with ID: " + id));
        
        payroll.setPaymentStatus(Payroll.PaymentStatus.APPROVED);
        payroll.setApprovedBy(approvedBy);
        payroll.setApprovedDate(LocalDateTime.now());
        payroll.setUpdatedAt(LocalDateTime.now());
        
        Payroll approvedPayroll = payrollRepository.save(payroll);
        log.info("Payroll approved successfully with ID: {}", approvedPayroll.getId());
        
        return mapToDTO(approvedPayroll);
    }
    
    @Override
    public PayrollDTO processPayment(String id) {
        log.info("Processing payment for payroll ID: {}", id);
        
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with ID: " + id));
        
        if (payroll.getPaymentStatus() != Payroll.PaymentStatus.APPROVED) {
            throw new IllegalStateException("Payroll must be approved before processing payment");
        }
        
        payroll.setPaymentStatus(Payroll.PaymentStatus.PAID);
        payroll.setTransactionReference("TXN" + System.currentTimeMillis());
        payroll.setUpdatedAt(LocalDateTime.now());
        
        Payroll paidPayroll = payrollRepository.save(payroll);
        log.info("Payment processed successfully for payroll ID: {}", paidPayroll.getId());
        
        return mapToDTO(paidPayroll);
    }
    
    @Override
    public void deletePayroll(String id) {
        if (!payrollRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payroll not found with ID: " + id);
        }
        payrollRepository.deleteById(id);
        log.info("Payroll deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmployeeAndPeriod(String employeeId, Integer month, Integer year) {
        return payrollRepository.existsByEmployeeIdAndMonthAndYear(employeeId, month, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPayrollCountByStatus(Payroll.PaymentStatus paymentStatus) {
        return payrollRepository.countByPaymentStatus(paymentStatus);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPayrollCountByPeriod(Integer month, Integer year) {
        return payrollRepository.countByMonthAndYear(month, year);
    }
    
    @Override
    public List<PayrollDTO> generateBulkPayroll(Integer month, Integer year) {
        log.info("Generating bulk payroll for period: {}/{}", month, year);
        
        // This would typically fetch all active employees and generate payroll for each
        // For now, returning empty list as implementation would require employee service integration
        return List.of();
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] generatePayslipPDF(String payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with ID: " + payrollId));
        
        return pdfGenerator.generatePayslip(payroll);
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] generatePayrollReportExcel(Integer month, Integer year) {
        List<Payroll> payrolls = payrollRepository.findByMonthAndYear(month, year);
        return excelGenerator.generatePayrollReport(payrolls);
    }
    
    private void calculatePayrollAmounts(Payroll payroll) {
        // Calculate gross salary
        BigDecimal grossSalary = payroll.getBasicSalary();
        if (payroll.getAllowances() != null) {
            grossSalary = grossSalary.add(
                    payroll.getAllowances().stream()
                            .map(Payroll.Allowance::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        }
        payroll.setGrossSalary(grossSalary);
        
        // Calculate total deductions
        BigDecimal totalDeductions = BigDecimal.ZERO;
        if (payroll.getDeductions() != null) {
            totalDeductions = payroll.getDeductions().stream()
                    .map(Payroll.Deduction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        
        // Calculate tax
        Payroll.TaxDetails taxDetails = taxCalculationService.calculateTax(grossSalary);
        payroll.setTaxDetails(taxDetails);
        totalDeductions = totalDeductions.add(taxDetails.getTotalTax());
        
        payroll.setTotalDeductions(totalDeductions);
        
        // Calculate net salary
        BigDecimal netSalary = grossSalary.subtract(totalDeductions);
        payroll.setNetSalary(netSalary);
    }
    
    private Payroll mapToEntity(PayrollDTO dto) {
        return Payroll.builder()
                .id(dto.getId())
                .employeeId(dto.getEmployeeId())
                .employeeCode(dto.getEmployeeCode())
                .employeeName(dto.getEmployeeName())
                .month(dto.getMonth())
                .year(dto.getYear())
                .paymentDate(dto.getPaymentDate())
                .basicSalary(dto.getBasicSalary())
                .allowances(mapAllowancesToEntity(dto.getAllowances()))
                .deductions(mapDeductionsToEntity(dto.getDeductions()))
                .grossSalary(dto.getGrossSalary())
                .totalDeductions(dto.getTotalDeductions())
                .netSalary(dto.getNetSalary())
                .taxDetails(mapTaxDetailsToEntity(dto.getTaxDetails()))
                .paymentStatus(dto.getPaymentStatus())
                .paymentMethod(dto.getPaymentMethod())
                .transactionReference(dto.getTransactionReference())
                .bankAccountNumber(dto.getBankAccountNumber())
                .remarks(dto.getRemarks())
                .createdBy(dto.getCreatedBy())
                .approvedBy(dto.getApprovedBy())
                .approvedDate(dto.getApprovedDate())
                .build();
    }
    
    private PayrollDTO mapToDTO(Payroll payroll) {
        return PayrollDTO.builder()
                .id(payroll.getId())
                .employeeId(payroll.getEmployeeId())
                .employeeCode(payroll.getEmployeeCode())
                .employeeName(payroll.getEmployeeName())
                .month(payroll.getMonth())
                .year(payroll.getYear())
                .paymentDate(payroll.getPaymentDate())
                .basicSalary(payroll.getBasicSalary())
                .allowances(mapAllowancesToDTO(payroll.getAllowances()))
                .deductions(mapDeductionsToDTO(payroll.getDeductions()))
                .grossSalary(payroll.getGrossSalary())
                .totalDeductions(payroll.getTotalDeductions())
                .netSalary(payroll.getNetSalary())
                .taxDetails(mapTaxDetailsToDTO(payroll.getTaxDetails()))
                .paymentStatus(payroll.getPaymentStatus())
                .paymentMethod(payroll.getPaymentMethod())
                .transactionReference(payroll.getTransactionReference())
                .bankAccountNumber(payroll.getBankAccountNumber())
                .remarks(payroll.getRemarks())
                .createdAt(payroll.getCreatedAt())
                .updatedAt(payroll.getUpdatedAt())
                .createdBy(payroll.getCreatedBy())
                .approvedBy(payroll.getApprovedBy())
                .approvedDate(payroll.getApprovedDate())
                .build();
    }
    
    private List<Payroll.Allowance> mapAllowancesToEntity(List<PayrollDTO.AllowanceDTO> allowanceDTOs) {
        if (allowanceDTOs == null) return null;
        return allowanceDTOs.stream()
                .map(dto -> Payroll.Allowance.builder()
                        .name(dto.getName())
                        .amount(dto.getAmount())
                        .type(dto.getType())
                        .build())
                .collect(Collectors.toList());
    }
    
    private List<PayrollDTO.AllowanceDTO> mapAllowancesToDTO(List<Payroll.Allowance> allowances) {
        if (allowances == null) return null;
        return allowances.stream()
                .map(allowance -> PayrollDTO.AllowanceDTO.builder()
                        .name(allowance.getName())
                        .amount(allowance.getAmount())
                        .type(allowance.getType())
                        .build())
                .collect(Collectors.toList());
    }
    
    private List<Payroll.Deduction> mapDeductionsToEntity(List<PayrollDTO.DeductionDTO> deductionDTOs) {
        if (deductionDTOs == null) return null;
        return deductionDTOs.stream()
                .map(dto -> Payroll.Deduction.builder()
                        .name(dto.getName())
                        .amount(dto.getAmount())
                        .type(dto.getType())
                        .build())
                .collect(Collectors.toList());
    }
    
    private List<PayrollDTO.DeductionDTO> mapDeductionsToDTO(List<Payroll.Deduction> deductions) {
        if (deductions == null) return null;
        return deductions.stream()
                .map(deduction -> PayrollDTO.DeductionDTO.builder()
                        .name(deduction.getName())
                        .amount(deduction.getAmount())
                        .type(deduction.getType())
                        .build())
                .collect(Collectors.toList());
    }
    
    private Payroll.TaxDetails mapTaxDetailsToEntity(PayrollDTO.TaxDetailsDTO taxDetailsDTO) {
        if (taxDetailsDTO == null) return null;
        return Payroll.TaxDetails.builder()
                .taxableIncome(taxDetailsDTO.getTaxableIncome())
                .incomeTax(taxDetailsDTO.getIncomeTax())
                .professionalTax(taxDetailsDTO.getProfessionalTax())
                .otherTaxes(taxDetailsDTO.getOtherTaxes())
                .totalTax(taxDetailsDTO.getTotalTax())
                .taxSlab(taxDetailsDTO.getTaxSlab())
                .build();
    }
    
    private PayrollDTO.TaxDetailsDTO mapTaxDetailsToDTO(Payroll.TaxDetails taxDetails) {
        if (taxDetails == null) return null;
        return PayrollDTO.TaxDetailsDTO.builder()
                .taxableIncome(taxDetails.getTaxableIncome())
                .incomeTax(taxDetails.getIncomeTax())
                .professionalTax(taxDetails.getProfessionalTax())
                .otherTaxes(taxDetails.getOtherTaxes())
                .totalTax(taxDetails.getTotalTax())
                .taxSlab(taxDetails.getTaxSlab())
                .build();
    }
    
    private void updatePayrollFields(Payroll payroll, PayrollDTO dto) {
        payroll.setEmployeeId(dto.getEmployeeId());
        payroll.setEmployeeCode(dto.getEmployeeCode());
        payroll.setEmployeeName(dto.getEmployeeName());
        payroll.setMonth(dto.getMonth());
        payroll.setYear(dto.getYear());
        payroll.setPaymentDate(dto.getPaymentDate());
        payroll.setBasicSalary(dto.getBasicSalary());
        payroll.setAllowances(mapAllowancesToEntity(dto.getAllowances()));
        payroll.setDeductions(mapDeductionsToEntity(dto.getDeductions()));
        payroll.setPaymentStatus(dto.getPaymentStatus());
        payroll.setPaymentMethod(dto.getPaymentMethod());
        payroll.setTransactionReference(dto.getTransactionReference());
        payroll.setBankAccountNumber(dto.getBankAccountNumber());
        payroll.setRemarks(dto.getRemarks());
    }
}
