package com.ems.payroll_service.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "attendance")
@Data
public class Attendance {

    @Id
    private String id;

    private String employeeId;
    private String employeeName;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDateTime breakStartTime;
    private LocalDateTime breakEndTime;

    // Calculated fields
    private Double hoursWorked;
    private Double overtimeHours;
    private Double breakHours;

    private String status; // PRESENT, ABSENT, LATE, HALF_DAY, HOLIDAY, LEAVE
    private String remarks;

    // Location tracking
    private String checkInLocation;
    private String checkOutLocation;

    // Approval
    private String approvedBy;
    private LocalDateTime approvedAt;
    private Boolean isApproved;
}
