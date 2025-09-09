package com.ems.payroll_service.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Data
public class AttendanceDto {

    private String id;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    private String employeeName;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDateTime breakStartTime;
    private LocalDateTime breakEndTime;

    // Calculated fields
    @DecimalMin(value = "0.0", message = "Hours worked must be greater than or equal to 0")
    @DecimalMax(value = "24.0", message = "Hours worked cannot exceed 24")
    private Double hoursWorked;

    @DecimalMin(value = "0.0", message = "Overtime hours must be greater than or equal to 0")
    private Double overtimeHours = 0.0;

    @DecimalMin(value = "0.0", message = "Break hours must be greater than or equal to 0")
    private Double breakHours = 0.0;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(PRESENT|ABSENT|LATE|HALF_DAY|HOLIDAY|LEAVE)$",
             message = "Status must be one of: PRESENT, ABSENT, LATE, HALF_DAY, HOLIDAY, LEAVE")
    private String status;

    private String remarks;

    // Location tracking
    private String checkInLocation;
    private String checkOutLocation;

    // Approval
    private String approvedBy;
    private Boolean isApproved = false;
}
