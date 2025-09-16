package com.chitkara.payroll_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chitkara.payroll_service.entity.EmployeeSalaryComponent;
import com.chitkara.payroll_service.repository.EmployeeSalaryComponentRepository;

@RestController
@RequestMapping("/api/employees")
public class EmployeeSalaryComponentController {

    private final EmployeeSalaryComponentRepository repository;

    public EmployeeSalaryComponentController(EmployeeSalaryComponentRepository repository) {
        this.repository = repository;
    }

    // ------------------ GET: All components for an employee ------------------
    @GetMapping("/{empId}/salary-components")
    public List<EmployeeSalaryComponent> getByEmployee(@PathVariable Long empId) {
        return repository.findByEmployeeId(empId);
    }

    // ------------------ POST: Add new component for an employee ------------------
    @PostMapping("/{empId}/salary-components")
    public EmployeeSalaryComponent addComponent(@PathVariable Long empId,
                                                @RequestBody EmployeeSalaryComponent esc) {
        esc.setEmployeeId(empId); // Set employee ID
        return repository.save(esc);
    }

    // ------------------ PUT: Edit a salary component ------------------
    @PutMapping("/salary-components/{compId}")
    public EmployeeSalaryComponent updateComponent(@PathVariable Long compId,
                                                   @RequestBody EmployeeSalaryComponent esc) {
        EmployeeSalaryComponent existing = repository.findById(compId)
                .orElseThrow(() -> new RuntimeException("Component not found with id: " + compId));
        existing.setAmount(esc.getAmount());
        existing.setSalaryComponent(esc.getSalaryComponent());
        return repository.save(existing);
    }

    // ------------------ DELETE: Remove a salary component ------------------
    @DeleteMapping("/salary-components/{compId}")
    public void deleteComponent(@PathVariable Long compId) {
        repository.deleteById(compId);
    }
}
