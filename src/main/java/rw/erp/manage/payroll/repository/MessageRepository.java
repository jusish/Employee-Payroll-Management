package rw.erp.manage.payroll.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.erp.manage.payroll.model.Employee;
import rw.erp.manage.payroll.model.Message;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Employee> findByEmployee(Employee employee);

}
