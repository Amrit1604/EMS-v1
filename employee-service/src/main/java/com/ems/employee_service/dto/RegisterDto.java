package com.ems.employee_service.dto;


import lombok.Data;

@Data
public class RegisterDto {
    private String fullName;
    private String email;
    private String password;
    // We can add other fields from the registration form here later
}