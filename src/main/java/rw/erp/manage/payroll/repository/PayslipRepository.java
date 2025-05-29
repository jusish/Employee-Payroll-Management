package rw.erp.manage.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.erp.manage.payroll.model.Payslip;

import java.util.List;
import java.util.UUID;

public interface PayslipRepository extends JpaRepository<Payslip, UUID> {
    List<Payslip> findByEmployeeIdAndMonthAndYear(UUID employeeId, Integer month, Integer year);

    List<Payslip> findByMonthAndYear(Integer month, Integer year);
}