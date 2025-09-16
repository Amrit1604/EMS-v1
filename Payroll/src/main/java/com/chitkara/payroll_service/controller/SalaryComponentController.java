package com.chitkara.payroll_service.controller;


import com.chitkara.payroll_service.entity.SalaryComponent;
import com.chitkara.payroll_service.repository.SalaryComponentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary-components")
public class SalaryComponentController {

    private final SalaryComponentRepository salaryComponentRepository;

    public SalaryComponentController(SalaryComponentRepository salaryComponentRepository) {
        this.salaryComponentRepository = salaryComponentRepository;
    }

    @PostMapping
    public SalaryComponent addSalaryComponent(@RequestBody SalaryComponent component) {
        return salaryComponentRepository.save(component);
    }

    @GetMapping
    public List<SalaryComponent> getAllSalaryComponents() {
        return salaryComponentRepository.findAll();
    }
}
