package com.ems.payroll_service.repositories;

import com.ems.payroll_service.models.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    Optional<Employee> findByEmployeeId(String employeeId);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartment(String department);
    List<Employee> findByStatus(String status);
    List<Employee> findByPosition(String position);
    List<Employee> findByManager(String manager);

    @Query("{'fullName': {$regex: ?0, $options: 'i'}}")
    List<Employee> findByFullNameContainingIgnoreCase(String name);

    @Query("{'department': ?0, 'status': ?1}")
    List<Employee> findByDepartmentAndStatus(String department, String status);

    @Query("{'employmentType': ?0}")
    List<Employee> findByEmploymentType(String employmentType);

    long countByStatus(String status);
    long countByDepartment(String department);
}
