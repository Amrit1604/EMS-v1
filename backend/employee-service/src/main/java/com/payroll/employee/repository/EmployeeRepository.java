package com.payroll.employee.repository;

import com.payroll.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    
    Optional<Employee> findByEmployeeCode(String employeeCode);
    
    Optional<Employee> findByEmail(String email);
    
    List<Employee> findByDepartmentId(String departmentId);
    
    List<Employee> findByDesignationId(String designationId);
    
    List<Employee> findByManagerId(String managerId);
    
    List<Employee> findByStatus(Employee.EmployeeStatus status);
    
    List<Employee> findByEmploymentType(Employee.EmploymentType employmentType);
    
    @Query("{'joiningDate': {$gte: ?0, $lte: ?1}}")
    List<Employee> findByJoiningDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("{'$or': [{'firstName': {$regex: ?0, $options: 'i'}}, {'lastName': {$regex: ?0, $options: 'i'}}, {'email': {$regex: ?0, $options: 'i'}}, {'employeeCode': {$regex: ?0, $options: 'i'}}]}")
    Page<Employee> findBySearchTerm(String searchTerm, Pageable pageable);
    
    @Query("{'department.id': ?0, 'status': ?1}")
    List<Employee> findByDepartmentIdAndStatus(String departmentId, Employee.EmployeeStatus status);
    
    boolean existsByEmployeeCode(String employeeCode);
    
    boolean existsByEmail(String email);
    
    long countByStatus(Employee.EmployeeStatus status);
    
    long countByDepartmentId(String departmentId);
}
