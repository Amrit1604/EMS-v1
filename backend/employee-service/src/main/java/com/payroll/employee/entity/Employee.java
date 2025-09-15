package com.payroll.employee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String employeeCode;
    
    private String firstName;
    
    private String lastName;
    
    @Indexed(unique = true)
    private String email;
    
    private String phoneNumber;
    
    private LocalDate dateOfBirth;
    
    private LocalDate joiningDate;
    
    @DBRef
    private Department department;
    
    @DBRef
    private Designation designation;
    
    private BigDecimal baseSalary;
    
    private EmployeeStatus status;
    
    private EmploymentType employmentType;
    
    @DBRef
    private Employee manager;
    
    @DBRef
    private Set<Employee> subordinates = new HashSet<>();
    
    private Address address;
    
    private BankDetails bankDetails;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private String createdBy;
    
    private String updatedBy;
    
    public enum EmployeeStatus {
        ACTIVE, INACTIVE, ON_LEAVE, TERMINATED, RESIGNED
    }
    
    public enum EmploymentType {
        FULL_TIME, PART_TIME, CONTRACT, INTERN
    }
}