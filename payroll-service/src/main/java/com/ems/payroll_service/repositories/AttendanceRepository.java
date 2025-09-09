package com.ems.payroll_service.repositories;

import com.ems.payroll_service.models.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {

    List<Attendance> findByEmployeeId(String employeeId);
    List<Attendance> findByDate(LocalDate date);
    Optional<Attendance> findByEmployeeIdAndDate(String employeeId, LocalDate date);
    List<Attendance> findByStatus(String status);

    @Query("{'employeeId': ?0, 'date': {$gte: ?1, $lte: ?2}}")
    List<Attendance> findByEmployeeIdAndDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);

    @Query("{'date': {$gte: ?0, $lte: ?1}}")
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("{'employeeId': ?0, 'status': ?1}")
    List<Attendance> findByEmployeeIdAndStatus(String employeeId, String status);

    @Query("{'date': {$gte: ?0, $lte: ?1}, 'status': ?2}")
    List<Attendance> findByDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, String status);

    // Count queries for statistics
    @Query(value = "{'employeeId': ?0, 'date': {$gte: ?1, $lte: ?2}, 'status': 'PRESENT'}", count = true)
    long countPresentDaysByEmployeeIdAndDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);

    @Query(value = "{'employeeId': ?0, 'date': {$gte: ?1, $lte: ?2}, 'overtimeHours': {$gt: 0}}", count = true)
    long countOvertimeDaysByEmployeeIdAndDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);
}
