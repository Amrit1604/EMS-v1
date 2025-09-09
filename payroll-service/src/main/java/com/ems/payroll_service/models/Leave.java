package com.ems.payroll_service.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "leaves")
@Data
public class Leave {

    @Id
    private String id;

    private String employeeId;
    private String employeeName;
    private String leaveType; // ANNUAL, SICK, CASUAL, MATERNITY, PATERNITY, EMERGENCY
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED, CANCELLED

    // Application details
    private LocalDateTime appliedAt;
    private LocalDateTime approvedAt;
    private String approvedBy;
    private String rejectionReason;

    // Handover details
    private String handoverTo;
    private String handoverNotes;

    // Emergency contact (for emergency leaves)
    private String emergencyContact;
    private String emergencyContactPhone;

    // Supporting documents
    private String documentPath;
    private Boolean isPaid; // Whether this leave is paid or unpaid
}
