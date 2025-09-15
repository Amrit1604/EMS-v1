package com.payroll.employee.repository;

import com.payroll.employee.entity.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    
    Optional<Department> findByName(String name);
    
    boolean existsByName(String name);
}
