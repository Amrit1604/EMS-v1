package com.payroll.employee.service.impl;

import com.payroll.employee.dto.DesignationDTO;
import com.payroll.employee.entity.Designation;
import com.payroll.employee.exception.DuplicateResourceException;
import com.payroll.employee.exception.ResourceNotFoundException;
import com.payroll.employee.repository.DesignationRepository;
import com.payroll.employee.service.DesignationService;
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
public class DesignationServiceImpl implements DesignationService {
    
    private final DesignationRepository designationRepository;
    
    @Override
    public DesignationDTO createDesignation(DesignationDTO designationDTO) {
        log.info("Creating new designation: {}", designationDTO.getTitle());
        
        if (designationRepository.existsByTitle(designationDTO.getTitle())) {
            throw new DuplicateResourceException("Designation with title " + designationDTO.getTitle() + " already exists");
        }
        
        Designation designation = mapToEntity(designationDTO);
        Designation savedDesignation = designationRepository.save(designation);
        log.info("Designation created successfully with ID: {}", savedDesignation.getId());
        
        return mapToDTO(savedDesignation);
    }
    
    @Override
    public DesignationDTO updateDesignation(String id, DesignationDTO designationDTO) {
        log.info("Updating designation with ID: {}", id);
        
        Designation existingDesignation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with ID: " + id));
        
        if (!existingDesignation.getTitle().equals(designationDTO.getTitle()) &&
            designationRepository.existsByTitle(designationDTO.getTitle())) {
            throw new DuplicateResourceException("Designation with title " + designationDTO.getTitle() + " already exists");
        }
        
        existingDesignation.setTitle(designationDTO.getTitle());
        existingDesignation.setLevel(designationDTO.getLevel());
        existingDesignation.setDescription(designationDTO.getDescription());
        
        Designation updatedDesignation = designationRepository.save(existingDesignation);
        log.info("Designation updated successfully with ID: {}", updatedDesignation.getId());
        
        return mapToDTO(updatedDesignation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DesignationDTO getDesignationById(String id) {
        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found with ID: " + id));
        return mapToDTO(designation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DesignationDTO> getAllDesignations() {
        return designationRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteDesignation(String id) {
        if (!designationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Designation not found with ID: " + id);
        }
        designationRepository.deleteById(id);
        log.info("Designation deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByTitle(String title) {
        return designationRepository.existsByTitle(title);
    }
    
    private Designation mapToEntity(DesignationDTO dto) {
        return Designation.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .level(dto.getLevel())
                .description(dto.getDescription())
                .build();
    }
    
    private DesignationDTO mapToDTO(Designation designation) {
        return DesignationDTO.builder()
                .id(designation.getId())
                .title(designation.getTitle())
                .level(designation.getLevel())
                .description(designation.getDescription())
                .build();
    }
}
