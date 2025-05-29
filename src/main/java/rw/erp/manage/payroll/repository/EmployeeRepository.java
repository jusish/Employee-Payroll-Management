package rw.erp.manage.payroll.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.erp.manage.payroll.model.Employee;
import java.util.Optional;
import java.util.List;
import rw.erp.manage.payroll.enums.EEmployeeStatus;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByMobileNumber(String mobileNumber);

    List<Employee> findByEmployeeStatus(EEmployeeStatus employeeStatus);
}
