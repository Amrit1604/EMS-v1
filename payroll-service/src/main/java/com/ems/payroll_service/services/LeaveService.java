package com.ems.payroll_service.services;

import com.ems.payroll_service.dto.LeaveDto;
import com.ems.payroll_service.models.Employee;
import com.ems.payroll_service.models.Leave;
import com.ems.payroll_service.repositories.EmployeeRepository;
import com.ems.payroll_service.repositories.LeaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public List<LeaveDto> getAllLeaves() {
        log.info("Fetching all leave records");
        return leaveRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<LeaveDto> getLeaveById(String id) {
        log.info("Fetching leave by ID: {}", id);
        return leaveRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<LeaveDto> getLeavesByEmployeeId(String employeeId) {
        log.info("Fetching leaves for employee: {}", employeeId);
        return leaveRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LeaveDto> getLeavesByStatus(String status) {
        log.info("Fetching leaves by status: {}", status);
        return leaveRepository.findByStatus(status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<LeaveDto> getLeavesByEmployeeAndDateRange(String employeeId,
                                                         LocalDate startDate,
                                                         LocalDate endDate) {
        log.info("Fetching leaves for employee: {} between {} and {}", employeeId, startDate, endDate);
        return leaveRepository.findByEmployeeIdAndStartDateBetween(employeeId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LeaveDto applyLeave(LeaveDto leaveDto) {
        log.info("Applying leave for employee: {} from {} to {}",
                leaveDto.getEmployeeId(), leaveDto.getStartDate(), leaveDto.getEndDate());

        // Check if employee exists
        Employee employee = employeeRepository.findByEmployeeId(leaveDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + leaveDto.getEmployeeId()));

        // Validate dates
        if (leaveDto.getEndDate().isBefore(leaveDto.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        // Calculate total days
        int totalDays = (int) ChronoUnit.DAYS.between(leaveDto.getStartDate(), leaveDto.getEndDate()) + 1;
        leaveDto.setTotalDays(totalDays);

        // Check for overlapping leaves
        List<Leave> overlappingLeaves = leaveRepository.findOverlappingLeaves(
                leaveDto.getEmployeeId(), leaveDto.getStartDate(), leaveDto.getEndDate());

        if (!overlappingLeaves.isEmpty()) {
            throw new RuntimeException("Leave dates overlap with existing leave application");
        }

        // Check leave balance
        if (!checkLeaveBalance(employee, leaveDto.getLeaveType(), totalDays)) {
            throw new RuntimeException("Insufficient leave balance for " + leaveDto.getLeaveType());
        }

        Leave leave = convertToEntity(leaveDto);
        leave.setEmployeeName(employee.getFullName());
        leave.setAppliedAt(LocalDateTime.now());
        leave.setStatus("PENDING");

        Leave savedLeave = leaveRepository.save(leave);
        log.info("Leave application created successfully with ID: {}", savedLeave.getId());

        return convertToDto(savedLeave);
    }

    public LeaveDto updateLeave(String id, LeaveDto leaveDto) {
        log.info("Updating leave with ID: {}", id);

        Leave existingLeave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id));

        // Don't allow update if already approved
        if ("APPROVED".equals(existingLeave.getStatus())) {
            throw new RuntimeException("Cannot update approved leave");
        }

        // Recalculate total days if dates are changed
        if (!leaveDto.getStartDate().equals(existingLeave.getStartDate()) ||
            !leaveDto.getEndDate().equals(existingLeave.getEndDate())) {

            int totalDays = (int) ChronoUnit.DAYS.between(leaveDto.getStartDate(), leaveDto.getEndDate()) + 1;
            leaveDto.setTotalDays(totalDays);

            // Check for overlapping leaves (excluding current leave)
            List<Leave> overlappingLeaves = leaveRepository.findOverlappingLeaves(
                    leaveDto.getEmployeeId(), leaveDto.getStartDate(), leaveDto.getEndDate())
                    .stream()
                    .filter(leave -> !leave.getId().equals(id))
                    .collect(Collectors.toList());

            if (!overlappingLeaves.isEmpty()) {
                throw new RuntimeException("Leave dates overlap with existing leave application");
            }
        }

        BeanUtils.copyProperties(leaveDto, existingLeave, "id", "employeeName", "appliedAt");

        Leave updatedLeave = leaveRepository.save(existingLeave);
        log.info("Leave updated successfully: {}", updatedLeave.getId());

        return convertToDto(updatedLeave);
    }

    public void deleteLeave(String id) {
        log.info("Deleting leave with ID: {}", id);

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id));

        if ("APPROVED".equals(leave.getStatus())) {
            throw new RuntimeException("Cannot delete approved leave");
        }

        leaveRepository.deleteById(id);
        log.info("Leave deleted successfully: {}", id);
    }

    public LeaveDto approveLeave(String id, String approvedBy) {
        log.info("Approving leave with ID: {} by: {}", id, approvedBy);

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id));

        if (!"PENDING".equals(leave.getStatus())) {
            throw new RuntimeException("Only pending leaves can be approved");
        }

        // Update employee leave balance
        Employee employee = employeeRepository.findByEmployeeId(leave.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + leave.getEmployeeId()));

        deductLeaveBalance(employee, leave.getLeaveType(), leave.getTotalDays());
        employeeRepository.save(employee);

        leave.setStatus("APPROVED");
        leave.setApprovedBy(approvedBy);
        leave.setApprovedAt(LocalDateTime.now());

        Leave approvedLeave = leaveRepository.save(leave);
        log.info("Leave approved successfully: {}", approvedLeave.getId());

        return convertToDto(approvedLeave);
    }

    public LeaveDto rejectLeave(String id, String rejectedBy, String rejectionReason) {
        log.info("Rejecting leave with ID: {} by: {}", id, rejectedBy);

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id));

        if (!"PENDING".equals(leave.getStatus())) {
            throw new RuntimeException("Only pending leaves can be rejected");
        }

        leave.setStatus("REJECTED");
        leave.setApprovedBy(rejectedBy);
        leave.setApprovedAt(LocalDateTime.now());
        leave.setRejectionReason(rejectionReason);

        Leave rejectedLeave = leaveRepository.save(leave);
        log.info("Leave rejected successfully: {}", rejectedLeave.getId());

        return convertToDto(rejectedLeave);
    }

    public LeaveDto cancelLeave(String id) {
        log.info("Cancelling leave with ID: {}", id);

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + id));

        if ("CANCELLED".equals(leave.getStatus())) {
            throw new RuntimeException("Leave is already cancelled");
        }

        // If leave was approved, restore the leave balance
        if ("APPROVED".equals(leave.getStatus())) {
            Employee employee = employeeRepository.findByEmployeeId(leave.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found: " + leave.getEmployeeId()));

            restoreLeaveBalance(employee, leave.getLeaveType(), leave.getTotalDays());
            employeeRepository.save(employee);
        }

        leave.setStatus("CANCELLED");

        Leave cancelledLeave = leaveRepository.save(leave);
        log.info("Leave cancelled successfully: {}", cancelledLeave.getId());

        return convertToDto(cancelledLeave);
    }

    public int getLeaveBalance(String employeeId, String leaveType) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        return switch (leaveType.toUpperCase()) {
            case "ANNUAL" -> employee.getAnnualLeaveBalance();
            case "SICK" -> employee.getSickLeaveBalance();
            case "CASUAL" -> employee.getCasualLeaveBalance();
            default -> 0;
        };
    }

    private boolean checkLeaveBalance(Employee employee, String leaveType, int requestedDays) {
        int currentBalance = switch (leaveType.toUpperCase()) {
            case "ANNUAL" -> employee.getAnnualLeaveBalance();
            case "SICK" -> employee.getSickLeaveBalance();
            case "CASUAL" -> employee.getCasualLeaveBalance();
            case "MATERNITY", "PATERNITY", "EMERGENCY" -> Integer.MAX_VALUE; // No limit for these types
            default -> 0;
        };

        return currentBalance >= requestedDays;
    }

    private void deductLeaveBalance(Employee employee, String leaveType, int days) {
        switch (leaveType.toUpperCase()) {
            case "ANNUAL":
                employee.setAnnualLeaveBalance(employee.getAnnualLeaveBalance() - days);
                break;
            case "SICK":
                employee.setSickLeaveBalance(employee.getSickLeaveBalance() - days);
                break;
            case "CASUAL":
                employee.setCasualLeaveBalance(employee.getCasualLeaveBalance() - days);
                break;
            // No deduction for MATERNITY, PATERNITY, EMERGENCY
        }
    }

    private void restoreLeaveBalance(Employee employee, String leaveType, int days) {
        switch (leaveType.toUpperCase()) {
            case "ANNUAL":
                employee.setAnnualLeaveBalance(employee.getAnnualLeaveBalance() + days);
                break;
            case "SICK":
                employee.setSickLeaveBalance(employee.getSickLeaveBalance() + days);
                break;
            case "CASUAL":
                employee.setCasualLeaveBalance(employee.getCasualLeaveBalance() + days);
                break;
            // No restoration for MATERNITY, PATERNITY, EMERGENCY
        }
    }

    private LeaveDto convertToDto(Leave leave) {
        LeaveDto dto = new LeaveDto();
        BeanUtils.copyProperties(leave, dto);
        return dto;
    }

    private Leave convertToEntity(LeaveDto dto) {
        Leave leave = new Leave();
        BeanUtils.copyProperties(dto, leave, "id");
        return leave;
    }
}
