package com.chitkara.payroll_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "payroll_runs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId; // Store employee ID instead of entity reference
    private String employeeName; // Cache employee name for performance
    private LocalDate month;
    private Double totalSalary;
    private String salaryBreakdown;


}
