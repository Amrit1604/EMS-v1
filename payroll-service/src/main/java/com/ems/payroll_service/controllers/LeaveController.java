package com.ems.payroll_service.controllers;

import com.ems.payroll_service.dto.LeaveDto;
import com.ems.payroll_service.services.LeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class LeaveController {

    private final LeaveService leaveService;

    @GetMapping
    public ResponseEntity<List<LeaveDto>> getAllLeaves() {
        List<LeaveDto> leaves = leaveService.getAllLeaves();
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveDto> getLeaveById(@PathVariable String id) {
        return leaveService.getLeaveById(id)
                .map(leave -> ResponseEntity.ok(leave))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveDto>> getLeavesByEmployeeId(@PathVariable String employeeId) {
        List<LeaveDto> leaves = leaveService.getLeavesByEmployeeId(employeeId);
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveDto>> getLeavesByStatus(@PathVariable String status) {
        List<LeaveDto> leaves = leaveService.getLeavesByStatus(status);
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/employee/{employeeId}/range")
    public ResponseEntity<List<LeaveDto>> getLeavesByEmployeeAndDateRange(
            @PathVariable String employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<LeaveDto> leaves = leaveService.getLeavesByEmployeeAndDateRange(employeeId, startDate, endDate);
        return ResponseEntity.ok(leaves);
    }

    @PostMapping("/apply")
    public ResponseEntity<LeaveDto> applyLeave(@Valid @RequestBody LeaveDto leaveDto) {
        try {
            LeaveDto appliedLeave = leaveService.applyLeave(leaveDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(appliedLeave);
        } catch (RuntimeException e) {
            log.error("Error applying leave: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveDto> updateLeave(@PathVariable String id,
                                               @Valid @RequestBody LeaveDto leaveDto) {
        try {
            LeaveDto updatedLeave = leaveService.updateLeave(id, leaveDto);
            return ResponseEntity.ok(updatedLeave);
        } catch (RuntimeException e) {
            log.error("Error updating leave: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable String id) {
        try {
            leaveService.deleteLeave(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting leave: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveDto> approveLeave(@PathVariable String id,
                                                @RequestParam String approvedBy) {
        try {
            LeaveDto approvedLeave = leaveService.approveLeave(id, approvedBy);
            return ResponseEntity.ok(approvedLeave);
        } catch (RuntimeException e) {
            log.error("Error approving leave: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveDto> rejectLeave(@PathVariable String id,
                                               @RequestParam String rejectedBy,
                                               @RequestParam String rejectionReason) {
        try {
            LeaveDto rejectedLeave = leaveService.rejectLeave(id, rejectedBy, rejectionReason);
            return ResponseEntity.ok(rejectedLeave);
        } catch (RuntimeException e) {
            log.error("Error rejecting leave: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<LeaveDto> cancelLeave(@PathVariable String id) {
        try {
            LeaveDto cancelledLeave = leaveService.cancelLeave(id);
            return ResponseEntity.ok(cancelledLeave);
        } catch (RuntimeException e) {
            log.error("Error cancelling leave: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/employee/{employeeId}/balance/{leaveType}")
    public ResponseEntity<Integer> getLeaveBalance(@PathVariable String employeeId,
                                                  @PathVariable String leaveType) {
        int balance = leaveService.getLeaveBalance(employeeId, leaveType);
        return ResponseEntity.ok(balance);
    }
}
