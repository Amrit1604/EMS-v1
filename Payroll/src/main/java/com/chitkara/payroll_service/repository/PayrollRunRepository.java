package com.chitkara.payroll_service.repository;

import com.chitkara.payroll_service.entity.PayrollRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PayrollRunRepository extends JpaRepository<PayrollRun, Long> {
    List<PayrollRun> findByEmployeeId(Long employeeId);
    List<PayrollRun> findByMonth(LocalDate month);
}


