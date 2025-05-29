package rw.erp.manage.payroll.dto.request;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rw.erp.manage.payroll.enums.ERole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String mobileNumber;
    private LocalDate dob;
    private Set<ERole> roles;
}
