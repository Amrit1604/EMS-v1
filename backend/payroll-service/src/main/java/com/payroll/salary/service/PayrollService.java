package com.payroll.salary.service;

import com.payroll.salary.dto.PayrollDTO;
import com.payroll.salary.entity.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PayrollService {
    
    PayrollDTO createPayroll(PayrollDTO payrollDTO);
    
    PayrollDTO updatePayroll(String id, PayrollDTO payrollDTO);
    
    PayrollDTO getPayrollById(String id);
    
    List<PayrollDTO> getPayrollsByEmployeeId(String employeeId);
    
    List<PayrollDTO> getPayrollsByEmployeeCode(String employeeCode);
    
    PayrollDTO getPayrollByEmployeeAndPeriod(String employeeId, Integer month, Integer year);
    
    List<PayrollDTO> getPayrollsByPeriod(Integer month, Integer year);
    
    List<PayrollDTO> getPayrollsByStatus(Payroll.PaymentStatus paymentStatus);
    
    List<PayrollDTO> getPayrollsByDateRange(LocalDate startDate, LocalDate endDate);
    
    Page<PayrollDTO> getAllPayrolls(Pageable pageable);
    
    Page<PayrollDTO> searchPayrolls(String searchTerm, Pageable pageable);
    
    PayrollDTO generatePayroll(String employeeId, Integer month, Integer year);
    
    PayrollDTO approvePayroll(String id, String approvedBy);
    
    PayrollDTO processPayment(String id);
    
    void deletePayroll(String id);
    
    boolean existsByEmployeeAndPeriod(String employeeId, Integer month, Integer year);
    
    long getPayrollCountByStatus(Payroll.PaymentStatus paymentStatus);
    
    long getPayrollCountByPeriod(Integer month, Integer year);
    
    List<PayrollDTO> generateBulkPayroll(Integer month, Integer year);
    
    byte[] generatePayslipPDF(String payrollId);
    
    byte[] generatePayrollReportExcel(Integer month, Integer year);
}
