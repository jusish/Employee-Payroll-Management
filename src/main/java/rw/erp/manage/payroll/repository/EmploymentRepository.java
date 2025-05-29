package rw.erp.manage.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.erp.manage.payroll.enums.EEmploymentStatus;
import rw.erp.manage.payroll.model.Employee;
import rw.erp.manage.payroll.model.Employment;

import java.util.Optional;
import java.util.UUID;

public interface EmploymentRepository extends JpaRepository<Employment, UUID> {
    Optional<Employment> findByEmployeeAndStatus(Employee employee, EEmploymentStatus status);
}

