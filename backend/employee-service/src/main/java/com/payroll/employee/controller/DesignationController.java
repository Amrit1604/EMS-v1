package com.payroll.employee.controller;

import com.payroll.employee.dto.DesignationDTO;
import com.payroll.employee.service.DesignationService;
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
@RequestMapping("/api/designations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Designation Management", description = "APIs for managing designations")
@CrossOrigin(origins = "*")
public class DesignationController {
    
    private final DesignationService designationService;
    
    @PostMapping
    // @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Create a new designation", description = "Creates a new designation")
    public ResponseEntity<DesignationDTO> createDesignation(@Valid @RequestBody DesignationDTO designationDTO) {
        log.info("Creating new designation: {}", designationDTO.getTitle());
        DesignationDTO createdDesignation = designationService.createDesignation(designationDTO);
        return new ResponseEntity<>(createdDesignation, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN')")
    @Operation(summary = "Update a designation", description = "Updates an existing designation")
    public ResponseEntity<DesignationDTO> updateDesignation(
            @Parameter(description = "Designation ID") @PathVariable String id,
            @Valid @RequestBody DesignationDTO designationDTO) {
        log.info("Updating designation with ID: {}", id);
        DesignationDTO updatedDesignation = designationService.updateDesignation(id, designationDTO);
        return ResponseEntity.ok(updatedDesignation);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get designation by ID", description = "Retrieves a designation by its ID")
    public ResponseEntity<DesignationDTO> getDesignationById(
            @Parameter(description = "Designation ID") @PathVariable String id) {
        DesignationDTO designation = designationService.getDesignationById(id);
        return ResponseEntity.ok(designation);
    }
    
    @GetMapping
    // @PreAuthorize("hasRole('HR') or hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get all designations", description = "Retrieves all designations")
    public ResponseEntity<List<DesignationDTO>> getAllDesignations() {
        List<DesignationDTO> designations = designationService.getAllDesignations();
        return ResponseEntity.ok(designations);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a designation", description = "Permanently deletes a designation")
    public ResponseEntity<Void> deleteDesignation(
            @Parameter(description = "Designation ID") @PathVariable String id) {
        log.info("Deleting designation with ID: {}", id);
        designationService.deleteDesignation(id);
        return ResponseEntity.noContent().build();
    }
}
