package com.payroll.employee.controller;

import com.payroll.employee.dto.EmployeeDTO;
import com.payroll.employee.entity.Employee;
import com.payroll.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employee Management", description = "APIs for managing employees")
@CrossOrigin(origins = "*")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    @PostMapping
    // @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new employee", description = "Creates a new employee record")
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("Creating new employee with code: {}", employeeDTO.getEmployeeCode());
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Update an employee", description = "Updates an existing employee record")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @Parameter(description = "Employee ID") @PathVariable String id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("Updating employee with ID: {}", id);
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee by their ID")
    public ResponseEntity<EmployeeDTO> getEmployeeById(
            @Parameter(description = "Employee ID") @PathVariable String id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
    
    @GetMapping("/code/{employeeCode}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get employee by code", description = "Retrieves an employee by their employee code")
    public ResponseEntity<EmployeeDTO> getEmployeeByCode(
            @Parameter(description = "Employee Code") @PathVariable String employeeCode) {
        EmployeeDTO employee = employeeService.getEmployeeByCode(employeeCode);
        return ResponseEntity.ok(employee);
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get employee by email", description = "Retrieves an employee by their email")
    public ResponseEntity<EmployeeDTO> getEmployeeByEmail(
            @Parameter(description = "Employee Email") @PathVariable String email) {
        EmployeeDTO employee = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.ok(employee);
    }
    
    @GetMapping
    // @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get all employees", description = "Retrieves all employees with pagination")
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeDTO> employees = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Search employees", description = "Searches employees by name, email, or employee code")
    public ResponseEntity<Page<EmployeeDTO>> searchEmployees(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<EmployeeDTO> employees = employeeService.searchEmployees(searchTerm, pageable);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get employees by department", description = "Retrieves all employees in a specific department")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(
            @Parameter(description = "Department ID") @PathVariable String departmentId) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/designation/{designationId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get employees by designation", description = "Retrieves all employees with a specific designation")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDesignation(
            @Parameter(description = "Designation ID") @PathVariable String designationId) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByDesignation(designationId);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get employees by manager", description = "Retrieves all employees under a specific manager")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByManager(
            @Parameter(description = "Manager ID") @PathVariable String managerId) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByManager(managerId);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get employees by status", description = "Retrieves all employees with a specific status")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByStatus(
            @Parameter(description = "Employee Status") @PathVariable Employee.EmployeeStatus status) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/employment-type/{employmentType}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get employees by employment type", description = "Retrieves all employees with a specific employment type")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByEmploymentType(
            @Parameter(description = "Employment Type") @PathVariable Employee.EmploymentType employmentType) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByEmploymentType(employmentType);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/joining-date-range")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get employees by joining date range", description = "Retrieves employees who joined within a date range")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByJoiningDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByJoiningDateRange(startDate, endDate);
        return ResponseEntity.ok(employees);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an employee", description = "Permanently deletes an employee record")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Employee ID") @PathVariable String id) {
        log.info("Deleting employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Deactivate an employee", description = "Deactivates an employee account")
    public ResponseEntity<Void> deactivateEmployee(
            @Parameter(description = "Employee ID") @PathVariable String id) {
        log.info("Deactivating employee with ID: {}", id);
        employeeService.deactivateEmployee(id);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Activate an employee", description = "Activates an employee account")
    public ResponseEntity<Void> activateEmployee(
            @Parameter(description = "Employee ID") @PathVariable String id) {
        log.info("Activating employee with ID: {}", id);
        employeeService.activateEmployee(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/exists/code/{employeeCode}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Check if employee code exists", description = "Checks if an employee code already exists")
    public ResponseEntity<Boolean> existsByEmployeeCode(
            @Parameter(description = "Employee Code") @PathVariable String employeeCode) {
        boolean exists = employeeService.existsByEmployeeCode(employeeCode);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/exists/email/{email}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Check if email exists", description = "Checks if an email already exists")
    public ResponseEntity<Boolean> existsByEmail(
            @Parameter(description = "Email") @PathVariable String email) {
        boolean exists = employeeService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get employee count by status", description = "Gets the count of employees by status")
    public ResponseEntity<Long> getEmployeeCountByStatus(
            @Parameter(description = "Employee Status") @PathVariable Employee.EmployeeStatus status) {
        long count = employeeService.getEmployeeCountByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/count/department/{departmentId}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Get employee count by department", description = "Gets the count of employees in a department")
    public ResponseEntity<Long> getEmployeeCountByDepartment(
            @Parameter(description = "Department ID") @PathVariable String departmentId) {
        long count = employeeService.getEmployeeCountByDepartment(departmentId);
        return ResponseEntity.ok(count);
    }
}
