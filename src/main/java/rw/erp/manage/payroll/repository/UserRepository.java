package rw.erp.manage.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.erp.manage.payroll.model.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByEmailVerifiedFalse();

    Optional<Employee> findByVerificationCode(String verificationCode);
}
