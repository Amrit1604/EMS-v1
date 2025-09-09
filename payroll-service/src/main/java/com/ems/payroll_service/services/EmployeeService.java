package com.ems.payroll_service.services;

import com.ems.payroll_service.dto.EmployeeDto;
import com.ems.payroll_service.models.Employee;
import com.ems.payroll_service.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<EmployeeDto> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeDto> getEmployeeById(String id) {
        log.info("Fetching employee by ID: {}", id);
        return employeeRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<EmployeeDto> getEmployeeByEmployeeId(String employeeId) {
        log.info("Fetching employee by Employee ID: {}", employeeId);
        return employeeRepository.findByEmployeeId(employeeId)
                .map(this::convertToDto);
    }

    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Creating new employee: {}", employeeDto.getFullName());

        // Check if employee ID already exists
        if (employeeRepository.findByEmployeeId(employeeDto.getEmployeeId()).isPresent()) {
            throw new RuntimeException("Employee ID already exists: " + employeeDto.getEmployeeId());
        }

        // Check if email already exists
        if (employeeRepository.findByEmail(employeeDto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + employeeDto.getEmail());
        }

        Employee employee = convertToEntity(employeeDto);
        if (employee.getJoinDate() == null) {
            employee.setJoinDate(LocalDate.now());
        }

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());

        return convertToDto(savedEmployee);
    }

    public EmployeeDto updateEmployee(String id, EmployeeDto employeeDto) {
        log.info("Updating employee with ID: {}", id);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        // Check if email is being changed and if new email already exists
        if (!existingEmployee.getEmail().equals(employeeDto.getEmail())) {
            if (employeeRepository.findByEmail(employeeDto.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists: " + employeeDto.getEmail());
            }
        }

        // Update fields
        BeanUtils.copyProperties(employeeDto, existingEmployee, "id", "joinDate");

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee updated successfully: {}", updatedEmployee.getId());

        return convertToDto(updatedEmployee);
    }

    public void deleteEmployee(String id) {
        log.info("Deleting employee with ID: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));

        // Instead of hard delete, mark as TERMINATED
        employee.setStatus("TERMINATED");
        employeeRepository.save(employee);

        log.info("Employee marked as terminated: {}", id);
    }

    public List<EmployeeDto> getEmployeesByDepartment(String department) {
        log.info("Fetching employees by department: {}", department);
        return employeeRepository.findByDepartment(department)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDto> getEmployeesByStatus(String status) {
        log.info("Fetching employees by status: {}", status);
        return employeeRepository.findByStatus(status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDto> searchEmployeesByName(String name) {
        log.info("Searching employees by name: {}", name);
        return employeeRepository.findByFullNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<EmployeeDto> getEmployeesByManager(String manager) {
        log.info("Fetching employees by manager: {}", manager);
        return employeeRepository.findByManager(manager)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public long getEmployeeCountByStatus(String status) {
        return employeeRepository.countByStatus(status);
    }

    public long getEmployeeCountByDepartment(String department) {
        return employeeRepository.countByDepartment(department);
    }

    // Helper methods
    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        BeanUtils.copyProperties(employee, dto);
        return dto;
    }

    private Employee convertToEntity(EmployeeDto dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee, "id");
        return employee;
    }
}
