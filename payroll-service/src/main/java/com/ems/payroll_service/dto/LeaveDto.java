package com.ems.payroll_service.dto;

import lombok.Data;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Data
public class LeaveDto {

    private String id;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    private String employeeName;

    @NotBlank(message = "Leave type is required")
    @Pattern(regexp = "^(ANNUAL|SICK|CASUAL|MATERNITY|PATERNITY|EMERGENCY)$",
             message = "Leave type must be one of: ANNUAL, SICK, CASUAL, MATERNITY, PATERNITY, EMERGENCY")
    private String leaveType;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Min(value = 1, message = "Total days must be at least 1")
    private Integer totalDays;

    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
    private String reason;

    private String status = "PENDING";

    // Handover details
    private String handoverTo;

    @Size(max = 1000, message = "Handover notes cannot exceed 1000 characters")
    private String handoverNotes;

    // Emergency contact (for emergency leaves)
    private String emergencyContact;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Please provide a valid emergency contact phone number")
    private String emergencyContactPhone;

    // Supporting documents
    private String documentPath;
    private Boolean isPaid = true;

    // For response
    private String approvedBy;
    private String rejectionReason;
}
