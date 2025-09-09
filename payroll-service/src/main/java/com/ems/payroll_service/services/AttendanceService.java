package com.ems.payroll_service.services;

import com.ems.payroll_service.dto.AttendanceDto;
import com.ems.payroll_service.models.Attendance;
import com.ems.payroll_service.models.Employee;
import com.ems.payroll_service.repositories.AttendanceRepository;
import com.ems.payroll_service.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public List<AttendanceDto> getAllAttendance() {
        log.info("Fetching all attendance records");
        return attendanceRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<AttendanceDto> getAttendanceById(String id) {
        log.info("Fetching attendance by ID: {}", id);
        return attendanceRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<AttendanceDto> getAttendanceByEmployeeId(String employeeId) {
        log.info("Fetching attendance for employee: {}", employeeId);
        return attendanceRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceDto> getAttendanceByDate(LocalDate date) {
        log.info("Fetching attendance for date: {}", date);
        return attendanceRepository.findByDate(date)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AttendanceDto> getAttendanceByEmployeeAndDateRange(String employeeId,
                                                                  LocalDate startDate,
                                                                  LocalDate endDate) {
        log.info("Fetching attendance for employee: {} between {} and {}", employeeId, startDate, endDate);
        return attendanceRepository.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AttendanceDto createAttendance(AttendanceDto attendanceDto) {
        log.info("Creating attendance for employee: {} on date: {}",
                attendanceDto.getEmployeeId(), attendanceDto.getDate());

        // Check if employee exists
        Employee employee = employeeRepository.findByEmployeeId(attendanceDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + attendanceDto.getEmployeeId()));

        // Check if attendance already exists for this employee and date
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByEmployeeIdAndDate(attendanceDto.getEmployeeId(), attendanceDto.getDate());

        if (existingAttendance.isPresent()) {
            throw new RuntimeException("Attendance already exists for employee " +
                    attendanceDto.getEmployeeId() + " on date " + attendanceDto.getDate());
        }

        Attendance attendance = convertToEntity(attendanceDto);
        attendance.setEmployeeName(employee.getFullName());

        // Calculate hours worked if check-in and check-out times are provided
        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            calculateHours(attendance);
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);
        log.info("Attendance created successfully with ID: {}", savedAttendance.getId());

        return convertToDto(savedAttendance);
    }

    public AttendanceDto updateAttendance(String id, AttendanceDto attendanceDto) {
        log.info("Updating attendance with ID: {}", id);

        Attendance existingAttendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found with ID: " + id));

        BeanUtils.copyProperties(attendanceDto, existingAttendance, "id", "employeeName");

        // Recalculate hours if times are updated
        if (existingAttendance.getCheckInTime() != null && existingAttendance.getCheckOutTime() != null) {
            calculateHours(existingAttendance);
        }

        Attendance updatedAttendance = attendanceRepository.save(existingAttendance);
        log.info("Attendance updated successfully: {}", updatedAttendance.getId());

        return convertToDto(updatedAttendance);
    }

    public void deleteAttendance(String id) {
        log.info("Deleting attendance with ID: {}", id);

        if (!attendanceRepository.existsById(id)) {
            throw new RuntimeException("Attendance not found with ID: " + id);
        }

        attendanceRepository.deleteById(id);
        log.info("Attendance deleted successfully: {}", id);
    }

    public AttendanceDto checkIn(String employeeId, LocalDateTime checkInTime, String location) {
        log.info("Check-in for employee: {} at: {}", employeeId, checkInTime);

        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        LocalDate today = checkInTime.toLocalDate();

        // Check if attendance already exists for today
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, today);

        if (existingAttendance.isPresent()) {
            throw new RuntimeException("Employee has already checked in today");
        }

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setEmployeeName(employee.getFullName());
        attendance.setDate(today);
        attendance.setCheckInTime(checkInTime);
        attendance.setCheckInLocation(location);
        attendance.setStatus("PRESENT");

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return convertToDto(savedAttendance);
    }

    public AttendanceDto checkOut(String employeeId, LocalDateTime checkOutTime, String location) {
        log.info("Check-out for employee: {} at: {}", employeeId, checkOutTime);

        LocalDate today = checkOutTime.toLocalDate();

        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> new RuntimeException("No check-in record found for today"));

        if (attendance.getCheckOutTime() != null) {
            throw new RuntimeException("Employee has already checked out today");
        }

        attendance.setCheckOutTime(checkOutTime);
        attendance.setCheckOutLocation(location);

        calculateHours(attendance);

        Attendance updatedAttendance = attendanceRepository.save(attendance);
        return convertToDto(updatedAttendance);
    }

    public AttendanceDto approveAttendance(String id, String approvedBy) {
        log.info("Approving attendance with ID: {} by: {}", id, approvedBy);

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found with ID: " + id));

        attendance.setIsApproved(true);
        attendance.setApprovedBy(approvedBy);
        attendance.setApprovedAt(LocalDateTime.now());

        Attendance approvedAttendance = attendanceRepository.save(attendance);
        return convertToDto(approvedAttendance);
    }

    public long getWorkingDaysForEmployee(String employeeId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.countPresentDaysByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
    }

    public double getOvertimeHoursForEmployee(String employeeId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendanceList = attendanceRepository
                .findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);

        return attendanceList.stream()
                .mapToDouble(attendance -> attendance.getOvertimeHours() != null ? attendance.getOvertimeHours() : 0.0)
                .sum();
    }

    private void calculateHours(Attendance attendance) {
        if (attendance.getCheckInTime() == null || attendance.getCheckOutTime() == null) {
            return;
        }

        Duration totalDuration = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
        double totalHours = totalDuration.toMinutes() / 60.0;

        // Calculate break hours if break times are provided
        double breakHours = 0.0;
        if (attendance.getBreakStartTime() != null && attendance.getBreakEndTime() != null) {
            Duration breakDuration = Duration.between(attendance.getBreakStartTime(), attendance.getBreakEndTime());
            breakHours = breakDuration.toMinutes() / 60.0;
        }

        // Ensure non-null
        attendance.setBreakHours(Math.round(breakHours * 10.0) / 10.0);

    // Calculate actual working hours
    double workingHours = totalHours - breakHours;
    // Round to 2 decimals for better precision
    workingHours = Math.max(0.0, Math.round(workingHours * 100.0) / 100.0);
    attendance.setHoursWorked(workingHours);

    // Calculate overtime (assuming 8 hours is standard working day)
    double overtimeHours = Math.max(0, workingHours - 8);
    overtimeHours = Math.round(overtimeHours * 100.0) / 100.0;
        attendance.setOvertimeHours(overtimeHours);

        // Determine status based on working hours
        if (workingHours >= 8) {
            attendance.setStatus("PRESENT");
        } else if (workingHours >= 4) {
            attendance.setStatus("HALF_DAY");
        } else if (workingHours > 0) {
            attendance.setStatus("LATE");
        } else {
            attendance.setStatus("ABSENT");
        }
    }

    private AttendanceDto convertToDto(Attendance attendance) {
        AttendanceDto dto = new AttendanceDto();
        BeanUtils.copyProperties(attendance, dto);
        return dto;
    }

    private Attendance convertToEntity(AttendanceDto dto) {
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(dto, attendance, "id");
        return attendance;
    }
}
