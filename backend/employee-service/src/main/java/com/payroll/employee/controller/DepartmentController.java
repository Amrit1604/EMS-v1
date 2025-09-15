package com.payroll.employee.controller;

import com.payroll.employee.dto.DepartmentDTO;
import com.payroll.employee.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Department Management", description = "APIs for managing departments")
@CrossOrigin(origins = "*")
public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    @PostMapping
    // @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new department", description = "Creates a new department")
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        log.info("Creating new department: {}", departmentDTO.getName());
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Update a department", description = "Updates an existing department")
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @Parameter(description = "Department ID") @PathVariable String id,
            @Valid @RequestBody DepartmentDTO departmentDTO) {
        log.info("Updating department with ID: {}", id);
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(id, departmentDTO);
        return ResponseEntity.ok(updatedDepartment);
    }
    
    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get department by ID", description = "Retrieves a department by its ID")
    public ResponseEntity<DepartmentDTO> getDepartmentById(
            @Parameter(description = "Department ID") @PathVariable String id) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }
    
    @GetMapping
    // @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get all departments", description = "Retrieves all departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a department", description = "Permanently deletes a department")
    public ResponseEntity<Void> deleteDepartment(
            @Parameter(description = "Department ID") @PathVariable String id) {
        log.info("Deleting department with ID: {}", id);
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
