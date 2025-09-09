package com.ems.employee_service.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}