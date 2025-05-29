package rw.erp.manage.payroll.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.erp.manage.payroll.model.Deductions;

public interface DeductionsRepository extends JpaRepository<Deductions, UUID> {
    Deductions findByDeductionName(String deductionName);
}