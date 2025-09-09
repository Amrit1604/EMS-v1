package com.ems.payroll_service.controllers;

import com.ems.payroll_service.dto.AttendanceDto;
import com.ems.payroll_service.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceDto>> getAllAttendance() {
        List<AttendanceDto> attendance = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDto> getAttendanceById(@PathVariable String id) {
        return attendanceService.getAttendanceById(id)
                .map(attendance -> ResponseEntity.ok(attendance))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByEmployeeId(@PathVariable String employeeId) {
        List<AttendanceDto> attendance = attendanceService.getAttendanceByEmployeeId(employeeId);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceDto> attendance = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/employee/{employeeId}/range")
    public ResponseEntity<List<AttendanceDto>> getAttendanceByEmployeeAndDateRange(
            @PathVariable String employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AttendanceDto> attendance = attendanceService.getAttendanceByEmployeeAndDateRange(
                employeeId, startDate, endDate);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping
    public ResponseEntity<AttendanceDto> createAttendance(@Valid @RequestBody AttendanceDto attendanceDto) {
        try {
            AttendanceDto createdAttendance = attendanceService.createAttendance(attendanceDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAttendance);
        } catch (RuntimeException e) {
            log.error("Error creating attendance: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDto> updateAttendance(@PathVariable String id,
                                                         @Valid @RequestBody AttendanceDto attendanceDto) {
        try {
            AttendanceDto updatedAttendance = attendanceService.updateAttendance(id, attendanceDto);
            return ResponseEntity.ok(updatedAttendance);
        } catch (RuntimeException e) {
            log.error("Error updating attendance: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable String id) {
        try {
            attendanceService.deleteAttendance(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting attendance: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/checkin")
    public ResponseEntity<AttendanceDto> checkIn(@RequestParam String employeeId,
                                                 @RequestParam(required = false) String location) {
        try {
            AttendanceDto attendance = attendanceService.checkIn(employeeId, LocalDateTime.now(), location);
            return ResponseEntity.status(HttpStatus.CREATED).body(attendance);
        } catch (RuntimeException e) {
            log.error("Error during check-in: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<AttendanceDto> checkOut(@RequestParam String employeeId,
                                                  @RequestParam(required = false) String location) {
        try {
            AttendanceDto attendance = attendanceService.checkOut(employeeId, LocalDateTime.now(), location);
            return ResponseEntity.ok(attendance);
        } catch (RuntimeException e) {
            log.error("Error during check-out: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<AttendanceDto> approveAttendance(@PathVariable String id,
                                                          @RequestParam String approvedBy) {
        try {
            AttendanceDto approvedAttendance = attendanceService.approveAttendance(id, approvedBy);
            return ResponseEntity.ok(approvedAttendance);
        } catch (RuntimeException e) {
            log.error("Error approving attendance: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/employee/{employeeId}/working-days")
    public ResponseEntity<Long> getWorkingDays(@PathVariable String employeeId,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        long workingDays = attendanceService.getWorkingDaysForEmployee(employeeId, startDate, endDate);
        return ResponseEntity.ok(workingDays);
    }

    @GetMapping("/employee/{employeeId}/overtime-hours")
    public ResponseEntity<Double> getOvertimeHours(@PathVariable String employeeId,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        double overtimeHours = attendanceService.getOvertimeHoursForEmployee(employeeId, startDate, endDate);
        return ResponseEntity.ok(overtimeHours);
    }
}
