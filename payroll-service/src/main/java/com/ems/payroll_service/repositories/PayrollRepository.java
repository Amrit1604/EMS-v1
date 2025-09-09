package com.ems.payroll_service.repositories;

import com.ems.payroll_service.models.Payroll;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends MongoRepository<Payroll, String> {

    List<Payroll> findByEmployeeId(String employeeId);
    List<Payroll> findByPayPeriod(String payPeriod);
    List<Payroll> findByStatus(String status);
    Optional<Payroll> findByEmployeeIdAndPayPeriod(String employeeId, String payPeriod);

    @Query("{'payDate': {$gte: ?0, $lte: ?1}}")
    List<Payroll> findByPayDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("{'employeeId': ?0, 'payDate': {$gte: ?1, $lte: ?2}}")
    List<Payroll> findByEmployeeIdAndPayDateBetween(String employeeId, LocalDate startDate, LocalDate endDate);

    @Query("{'status': ?0, 'payPeriod': ?1}")
    List<Payroll> findByStatusAndPayPeriod(String status, String payPeriod);

    @Query("{'approvedBy': ?0}")
    List<Payroll> findByApprovedBy(String approvedBy);

    // Aggregation queries for reports
    @Query(value = "{'payPeriod': ?0}", fields = "{'totalEarnings': 1, 'totalDeductions': 1, 'netPay': 1}")
    List<Payroll> findPayrollSummaryByPeriod(String payPeriod);
}
