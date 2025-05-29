package rw.erp.manage.payroll.dto.response;

import lombok.Data;
import rw.erp.manage.payroll.enums.EEmploymentStatus;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmploymentResponse {
    private UUID code;
    private UUID employeeId;
    private String department;
    private String position;
    private Double baseSalary;
    private EEmploymentStatus status;
    private LocalDate joiningDate;
}