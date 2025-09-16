package com.chitkara.employee_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chitkara.employee_service.entity.Department;
import com.chitkara.employee_service.entity.Employee;
import com.chitkara.employee_service.repository.DepartmentRepository;
import com.chitkara.employee_service.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository){
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public Employee addEmployee(Employee emp) {
        if(emp.getDepartment() != null && emp.getDepartment().getId() != null) {
            Department dept = departmentRepository.findById(emp.getDepartment().getId())
                              .orElseThrow(() -> new RuntimeException("Department not found"));
            emp.setDepartment(dept);
        }
        return employeeRepository.save(emp);
    }
    
    

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }
    public Employee updateEmployee(Employee emp) {
        Employee existing = employeeRepository.findById(emp.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        existing.setName(emp.getName());
        existing.setEmail(emp.getEmail());
        existing.setJoiningDate(emp.getJoiningDate());
        existing.setDepartment(emp.getDepartment());
        return employeeRepository.save(existing);
    }
    
    
}
