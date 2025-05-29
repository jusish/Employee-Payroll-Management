package rw.erp.manage.payroll.dto.request;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.erp.manage.payroll.enums.EEmployeeStatus;
import rw.erp.manage.payroll.enums.ERole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEditRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dob;
    private EEmployeeStatus status;
    private Set<ERole> roles;
}
