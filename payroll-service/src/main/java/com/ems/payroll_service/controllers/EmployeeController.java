package com.ems.payroll_service.controllers;

import com.ems.payroll_service.dto.EmployeeDto;
import com.ems.payroll_service.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id)
                .map(employee -> ResponseEntity.ok(employee))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee-id/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        return employeeService.getEmployeeByEmployeeId(employeeId)
                .map(employee -> ResponseEntity.ok(employee))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        try {
            EmployeeDto createdEmployee = employeeService.createEmployee(employeeDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        } catch (RuntimeException e) {
            log.error("Error creating employee: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable String id,
                                                     @Valid @RequestBody EmployeeDto employeeDto) {
        try {
            EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
            return ResponseEntity.ok(updatedEmployee);
        } catch (RuntimeException e) {
            log.error("Error updating employee: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting employee: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByDepartment(@PathVariable String department) {
        List<EmployeeDto> employees = employeeService.getEmployeesByDepartment(department);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByStatus(@PathVariable String status) {
        List<EmployeeDto> employees = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDto>> searchEmployeesByName(@RequestParam String name) {
        List<EmployeeDto> employees = employeeService.searchEmployeesByName(name);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/manager/{manager}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByManager(@PathVariable String manager) {
        List<EmployeeDto> employees = employeeService.getEmployeesByManager(manager);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/statistics/count")
    public ResponseEntity<Map<String, Object>> getEmployeeStatistics() {
        Map<String, Object> statistics = Map.of(
            "activeEmployees", employeeService.getEmployeeCountByStatus("ACTIVE"),
            "inactiveEmployees", employeeService.getEmployeeCountByStatus("INACTIVE"),
            "terminatedEmployees", employeeService.getEmployeeCountByStatus("TERMINATED")
        );
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/statistics/department")
    public ResponseEntity<Map<String, Object>> getDepartmentStatistics() {
        // This could be expanded to include all departments dynamically
        Map<String, Object> statistics = Map.of(
            "IT", employeeService.getEmployeeCountByDepartment("IT"),
            "HR", employeeService.getEmployeeCountByDepartment("HR"),
            "Finance", employeeService.getEmployeeCountByDepartment("Finance"),
            "Operations", employeeService.getEmployeeCountByDepartment("Operations")
        );
        return ResponseEntity.ok(statistics);
    }
}
