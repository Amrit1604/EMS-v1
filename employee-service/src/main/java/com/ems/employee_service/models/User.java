package com.ems.employee_service.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String department;
    private String position;
    private String joinDate;
    private String address;
    private String status; // e.g., "ACTIVE", "INACTIVE"

    private Set<String> roles; // e.g., ["ROLE_EMPLOYEE", "ROLE_ADMIN"]
}