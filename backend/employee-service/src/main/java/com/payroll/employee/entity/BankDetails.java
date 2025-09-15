package com.payroll.employee.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankDetails {
    
    private String accountNumber;
    
    private String accountHolderName;
    
    private String bankName;
    
    private String branchName;
    
    private String ifscCode;
    
    private String swiftCode;
}
