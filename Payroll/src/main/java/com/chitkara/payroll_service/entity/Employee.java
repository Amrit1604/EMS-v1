package com.chitkara.payroll_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees_cache") // Different table name to avoid conflicts
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    private Long id;
    private String name;
    private String email;

    // Remove any @ManyToOne or @OneToMany annotations that reference other entities
    // Just store the department ID as a simple field
    private Long departmentId;

    @Column(name = "joining_date")
    private LocalDate joiningDate; // This should be a simple field, not a relationship
}
