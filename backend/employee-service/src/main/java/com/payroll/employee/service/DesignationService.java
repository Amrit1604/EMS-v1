package com.payroll.employee.service;

import com.payroll.employee.dto.DesignationDTO;

import java.util.List;

public interface DesignationService {
    
    DesignationDTO createDesignation(DesignationDTO designationDTO);
    
    DesignationDTO updateDesignation(String id, DesignationDTO designationDTO);
    
    DesignationDTO getDesignationById(String id);
    
    List<DesignationDTO> getAllDesignations();
    
    void deleteDesignation(String id);
    
    boolean existsByTitle(String title);
}
