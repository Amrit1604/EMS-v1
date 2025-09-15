package com.payroll.employee.service;

import com.payroll.employee.dto.EmployeeDTO;
import com.payroll.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    
    EmployeeDTO updateEmployee(String id, EmployeeDTO employeeDTO);
    
    EmployeeDTO getEmployeeById(String id);
    
    EmployeeDTO getEmployeeByCode(String employeeCode);
    
    EmployeeDTO getEmployeeByEmail(String email);
    
    Page<EmployeeDTO> getAllEmployees(Pageable pageable);
    
    Page<EmployeeDTO> searchEmployees(String searchTerm, Pageable pageable);
    
    List<EmployeeDTO> getEmployeesByDepartment(String departmentId);
    
    List<EmployeeDTO> getEmployeesByDesignation(String designationId);
    
    List<EmployeeDTO> getEmployeesByManager(String managerId);
    
    List<EmployeeDTO> getEmployeesByStatus(Employee.EmployeeStatus status);
    
    List<EmployeeDTO> getEmployeesByEmploymentType(Employee.EmploymentType employmentType);
    
    List<EmployeeDTO> getEmployeesByJoiningDateRange(LocalDate startDate, LocalDate endDate);
    
    void deleteEmployee(String id);
    
    void deactivateEmployee(String id);
    
    void activateEmployee(String id);
    
    boolean existsByEmployeeCode(String employeeCode);
    
    boolean existsByEmail(String email);
    
    long getEmployeeCountByStatus(Employee.EmployeeStatus status);
    
    long getEmployeeCountByDepartment(String departmentId);
}
