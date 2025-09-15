package com.payroll.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignationDTO {
    
    private String id;
    
    @NotBlank(message = "Designation title is required")
    @Size(min = 2, max = 100, message = "Designation title must be between 2 and 100 characters")
    private String title;
    
    @Size(max = 50, message = "Level cannot exceed 50 characters")
    private String level;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}
