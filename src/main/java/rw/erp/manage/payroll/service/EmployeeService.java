package rw.erp.manage.payroll.service;

import java.util.List;
import java.util.UUID;

import rw.erp.manage.payroll.dto.request.EmployeeEditRequest;
import rw.erp.manage.payroll.dto.request.RegisterRequest;
import rw.erp.manage.payroll.model.Employee;

public interface EmployeeService {

    Employee findByEmail(String email);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(UUID id);

    Employee createEmployee(RegisterRequest employee);

    void deleteEmployee(UUID id);

    Employee updateEmployee(UUID id, EmployeeEditRequest employee);

}
