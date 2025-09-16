package com.chitkara.payroll_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chitkara.payroll_service.entity.EmployeeSalaryComponent;

public interface EmployeeSalaryComponentRepository extends JpaRepository<EmployeeSalaryComponent, Long> {
    List<EmployeeSalaryComponent> findByEmployeeId(Long employeeId);
}
