package com.payroll.employee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    
    private String streetAddress;
    
    private String city;
    
    private String state;
    
    private String country;
    
    private String postalCode;
}
