package com.payroll.employee.service.impl;

import com.payroll.employee.dto.EmployeeDTO;
import com.payroll.employee.entity.Department;
import com.payroll.employee.entity.Designation;
import com.payroll.employee.entity.Employee;
import com.payroll.employee.exception.ResourceNotFoundException;
import com.payroll.employee.exception.DuplicateResourceException;
import com.payroll.employee.repository.DepartmentRepository;
import com.payroll.employee.repository.DesignationRepository;
import com.payroll.employee.repository.EmployeeRepository;
import com.payroll.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        log.info("Creating new employee with code: {}", employeeDTO.getEmployeeCode());
        
        // Check for duplicates
        if (employeeRepository.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
            throw new DuplicateResourceException("Employee with code " + employeeDTO.getEmployeeCode() + " already exists");
        }
        
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new DuplicateResourceException("Employee with email " + employeeDTO.getEmail() + " already exists");
        }
        
        Employee employee = mapToEntity(employeeDTO);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());
        
        return mapToDTO(savedEmployee);
    }
    
    @Override
    public EmployeeDTO updateEmployee(String id, EmployeeDTO employeeDTO) {
        log.info("Updating employee with ID: {}", id);
        
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        
        // Check for duplicates (excluding current employee)
        if (!existingEmployee.getEmployeeCode().equals(employeeDTO.getEmployeeCode()) &&
            employeeRepository.existsByEmployeeCode(employeeDTO.getEmployeeCode())) {
            throw new DuplicateResourceException("Employee with code " + employeeDTO.getEmployeeCode() + " already exists");
        }
        
        if (!existingEmployee.getEmail().equals(employeeDTO.getEmail()) &&
            employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new DuplicateResourceException("Employee with email " + employeeDTO.getEmail() + " already exists");
        }
        
        updateEmployeeFields(existingEmployee, employeeDTO);
        existingEmployee.setUpdatedAt(LocalDateTime.now());
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee updated successfully with ID: {}", updatedEmployee.getId());
        
        return mapToDTO(updatedEmployee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return mapToDTO(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByCode(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));
        return mapToDTO(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
        return mapToDTO(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(this::mapToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> searchEmployees(String searchTerm, Pageable pageable) {
        return employeeRepository.findBySearchTerm(searchTerm, pageable).map(this::mapToDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(String departmentId) {
        return employeeRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDesignation(String designationId) {
        return employeeRepository.findByDesignationId(designationId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByManager(String managerId) {
        return employeeRepository.findByManagerId(managerId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByStatus(Employee.EmployeeStatus status) {
        return employeeRepository.findByStatus(status)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByEmploymentType(Employee.EmploymentType employmentType) {
        return employeeRepository.findByEmploymentType(employmentType)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByJoiningDateRange(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findByJoiningDateBetween(startDate, endDate)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteEmployee(String id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Employee deleted successfully with ID: {}", id);
    }
    
    @Override
    public void deactivateEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        
        employee.setStatus(Employee.EmployeeStatus.INACTIVE);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
        log.info("Employee deactivated successfully with ID: {}", id);
    }
    
    @Override
    public void activateEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        
        employee.setStatus(Employee.EmployeeStatus.ACTIVE);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
        log.info("Employee activated successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmployeeCode(String employeeCode) {
        return employeeRepository.existsByEmployeeCode(employeeCode);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getEmployeeCountByStatus(Employee.EmployeeStatus status) {
        return employeeRepository.countByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getEmployeeCountByDepartment(String departmentId) {
        return employeeRepository.countByDepartmentId(departmentId);
    }
    
    private Employee mapToEntity(EmployeeDTO dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));
        
        Designation designation = designationRepository.findById(dto.getDesignationId())
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with ID: " + dto.getDesignationId()));
        
        Employee manager = null;
        if (dto.getManagerId() != null) {
            manager = employeeRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + dto.getManagerId()));
        }
        
        return Employee.builder()
                .id(dto.getId())
                .employeeCode(dto.getEmployeeCode())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .dateOfBirth(dto.getDateOfBirth())
                .joiningDate(dto.getJoiningDate())
                .department(department)
                .designation(designation)
                .baseSalary(dto.getBaseSalary())
                .status(dto.getStatus())
                .employmentType(dto.getEmploymentType())
                .manager(manager)
                .address(dto.getAddress())
                .bankDetails(dto.getBankDetails())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }
    
    private EmployeeDTO mapToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .dateOfBirth(employee.getDateOfBirth())
                .joiningDate(employee.getJoiningDate())
                .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
                .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
                .designationId(employee.getDesignation() != null ? employee.getDesignation().getId() : null)
                .designationTitle(employee.getDesignation() != null ? employee.getDesignation().getTitle() : null)
                .baseSalary(employee.getBaseSalary())
                .status(employee.getStatus())
                .employmentType(employee.getEmploymentType())
                .managerId(employee.getManager() != null ? employee.getManager().getId() : null)
                .managerName(employee.getManager() != null ? 
                    employee.getManager().getFirstName() + " " + employee.getManager().getLastName() : null)
                .address(employee.getAddress())
                .bankDetails(employee.getBankDetails())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .createdBy(employee.getCreatedBy())
                .updatedBy(employee.getUpdatedBy())
                .build();
    }
    
    private void updateEmployeeFields(Employee employee, EmployeeDTO dto) {
        employee.setEmployeeCode(dto.getEmployeeCode());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setJoiningDate(dto.getJoiningDate());
        employee.setBaseSalary(dto.getBaseSalary());
        employee.setStatus(dto.getStatus());
        employee.setEmploymentType(dto.getEmploymentType());
        employee.setAddress(dto.getAddress());
        employee.setBankDetails(dto.getBankDetails());
        employee.setUpdatedBy(dto.getUpdatedBy());
        
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));
            employee.setDepartment(department);
        }
        
        if (dto.getDesignationId() != null) {
            Designation designation = designationRepository.findById(dto.getDesignationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation not found with ID: " + dto.getDesignationId()));
            employee.setDesignation(designation);
        }
        
        if (dto.getManagerId() != null) {
            Employee manager = employeeRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID: " + dto.getManagerId()));
            employee.setManager(manager);
        }
    }
}
