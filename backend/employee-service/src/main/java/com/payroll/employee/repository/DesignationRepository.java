package com.payroll.employee.repository;

import com.payroll.employee.entity.Designation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignationRepository extends MongoRepository<Designation, String> {
    
    Optional<Designation> findByTitle(String title);
    
    boolean existsByTitle(String title);
}
