package com.payroll.employee.service;

import com.payroll.employee.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    
    DepartmentDTO updateDepartment(String id, DepartmentDTO departmentDTO);
    
    DepartmentDTO getDepartmentById(String id);
    
    List<DepartmentDTO> getAllDepartments();
    
    void deleteDepartment(String id);
    
    boolean existsByName(String name);
}
