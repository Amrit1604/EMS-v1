package com.payroll.employee.service.impl;

import com.payroll.employee.dto.DepartmentDTO;
import com.payroll.employee.entity.Department;
import com.payroll.employee.exception.DuplicateResourceException;
import com.payroll.employee.exception.ResourceNotFoundException;
import com.payroll.employee.repository.DepartmentRepository;
import com.payroll.employee.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        log.info("Creating new department: {}", departmentDTO.getName());
        
        if (departmentRepository.existsByName(departmentDTO.getName())) {
            throw new DuplicateResourceException("Department with name " + departmentDTO.getName() + " already exists");
        }
        
        Department department = mapToEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        log.info("Department created successfully with ID: {}", savedDepartment.getId());
        
        return mapToDTO(savedDepartment);
    }
    
    @Override
    public DepartmentDTO updateDepartment(String id, DepartmentDTO departmentDTO) {
        log.info("Updating department with ID: {}", id);
        
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        
        if (!existingDepartment.getName().equals(departmentDTO.getName()) &&
            departmentRepository.existsByName(departmentDTO.getName())) {
            throw new DuplicateResourceException("Department with name " + departmentDTO.getName() + " already exists");
        }
        
        existingDepartment.setName(departmentDTO.getName());
        existingDepartment.setDescription(departmentDTO.getDescription());
        
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        log.info("Department updated successfully with ID: {}", updatedDepartment.getId());
        
        return mapToDTO(updatedDepartment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        return mapToDTO(department);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteDepartment(String id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }
        departmentRepository.deleteById(id);
        log.info("Department deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
    
    private Department mapToEntity(DepartmentDTO dto) {
        return Department.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
    
    private DepartmentDTO mapToDTO(Department department) {
        return DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
