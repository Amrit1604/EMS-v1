package com.chitkara.employee_service.controller;

import com.chitkara.employee_service.entity.Department;
import com.chitkara.employee_service.entity.Employee;
import com.chitkara.employee_service.repository.DepartmentRepository;
import com.chitkara.employee_service.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web")
public class WebController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Dashboard
    @GetMapping("/")
    public String dashboard(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        List<Department> departments = departmentRepository.findAll();

        model.addAttribute("employeeCount", employees.size());
        model.addAttribute("departmentCount", departments.size());
        model.addAttribute("recentEmployees", employees.subList(0, Math.min(5, employees.size())));

        return "dashboard";
    }

    // Employee Management
    @GetMapping("/employees")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employees/list";
    }

    @GetMapping("/employees/add")
    public String showAddEmployeeForm(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departments);
        return "employees/add";
    }

    @PostMapping("/employees/add")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeService.addEmployee(employee);
        return "redirect:/web/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departments);
        return "employees/edit";
    }

    @PostMapping("/employees/update")
    public String updateEmployee(@ModelAttribute Employee employee) {
        employeeService.addEmployee(employee); // This will update existing employee
        return "redirect:/web/employees";
    }

    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/web/employees";
    }

    // Department Management
    @GetMapping("/departments")
    public String listDepartments(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "departments/list";
    }

    @GetMapping("/departments/add")
    public String showAddDepartmentForm(Model model) {
        model.addAttribute("department", new Department());
        return "departments/add";
    }

    @PostMapping("/departments/add")
    public String addDepartment(@ModelAttribute Department department) {
        departmentRepository.save(department);
        return "redirect:/web/departments";
    }
}
