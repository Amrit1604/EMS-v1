package com.payroll.employee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "designations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Designation {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String title;
    
    private String level;
    
    private String description;
}
