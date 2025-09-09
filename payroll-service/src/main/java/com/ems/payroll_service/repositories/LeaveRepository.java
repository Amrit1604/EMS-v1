package com.ems.payroll_service.repositories;

import com.ems.payroll_service.models.Leave;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends MongoRepository<Leave, String> {

    List<Leave> findByEmployeeId(String employeeId);
    List<Leave> findByStatus(String status);
    List<Leave> findByLeaveType(String leaveType);
    List<Leave> findByApprovedBy(String approvedBy);

    @Query("{'employeeId': ?0, 'status': ?1}")
    List<Leave> findByEmployeeIdAndStatus(String employeeId, String status);

    @Query("{'employeeId': ?0, 'leaveType': ?1}")
    List<Leave> findByEmployeeIdAndLeaveType(String employeeId, String leaveType);

    @Query("{'startDate': {$gte: ?0, $lte: ?1}}")
    List<Leave> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("{'employeeId': ?0, 'startDate': {$gte: ?1, $lte: ?2}}")
    List<Leave> findByEmployeeIdAndStartDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);

    // Check for overlapping leaves
    @Query("{'employeeId': ?0, 'status': {$in: ['PENDING', 'APPROVED']}, " +
           "$or': [" +
           "{'startDate': {$lte: ?2}, 'endDate': {$gte: ?1}}" +
           "]}")
    List<Leave> findOverlappingLeaves(String employeeId, LocalDate startDate, LocalDate endDate);

    // Count queries for leave balance calculations
    @Query(value = "{'employeeId': ?0, 'leaveType': ?1, 'status': 'APPROVED', " +
                  "'startDate': {$gte: ?2, $lte: ?3}}",
           fields = "{'totalDays': 1}")
    List<Leave> findApprovedLeavesByEmployeeIdAndTypeAndDateRange(String employeeId, String leaveType,
                                                                 LocalDate startDate, LocalDate endDate);
}
