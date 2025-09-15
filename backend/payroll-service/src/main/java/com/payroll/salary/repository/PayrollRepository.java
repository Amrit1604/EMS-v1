package com.payroll.salary.repository;

import com.payroll.salary.entity.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends MongoRepository<Payroll, String> {
    
    List<Payroll> findByEmployeeId(String employeeId);
    
    List<Payroll> findByEmployeeCode(String employeeCode);
    
    Optional<Payroll> findByEmployeeIdAndMonthAndYear(String employeeId, Integer month, Integer year);
    
    List<Payroll> findByMonthAndYear(Integer month, Integer year);
    
    List<Payroll> findByPaymentStatus(Payroll.PaymentStatus paymentStatus);
    
    @Query("{'paymentDate': {$gte: ?0, $lte: ?1}}")
    List<Payroll> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("{'employeeId': ?0, 'paymentStatus': ?1}")
    List<Payroll> findByEmployeeIdAndPaymentStatus(String employeeId, Payroll.PaymentStatus paymentStatus);
    
    @Query("{'month': ?0, 'year': ?1, 'paymentStatus': ?2}")
    List<Payroll> findByMonthAndYearAndPaymentStatus(Integer month, Integer year, Payroll.PaymentStatus paymentStatus);
    
    @Query("{'$or': [{'employeeName': {$regex: ?0, $options: 'i'}}, {'employeeCode': {$regex: ?0, $options: 'i'}}]}")
    Page<Payroll> findBySearchTerm(String searchTerm, Pageable pageable);
    
    long countByPaymentStatus(Payroll.PaymentStatus paymentStatus);
    
    long countByMonthAndYear(Integer month, Integer year);
    
    @Query("{'year': ?0}")
    List<Payroll> findByYear(Integer year);
    
    boolean existsByEmployeeIdAndMonthAndYear(String employeeId, Integer month, Integer year);
}
