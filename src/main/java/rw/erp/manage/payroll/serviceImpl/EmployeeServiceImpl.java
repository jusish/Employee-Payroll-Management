package rw.erp.manage.payroll.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rw.erp.manage.payroll.dto.request.EmployeeEditRequest;
import rw.erp.manage.payroll.dto.request.RegisterRequest;
import rw.erp.manage.payroll.enums.EEmployeeStatus;
import rw.erp.manage.payroll.model.Employee;
import rw.erp.manage.payroll.repository.EmployeeRepository;
import rw.erp.manage.payroll.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email).orElse(null);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(UUID id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Employee createEmployee(RegisterRequest employee) {
        Employee newEmployee = new Employee();
        newEmployee.setFirst_name(employee.getFirst_name());
        newEmployee.setLast_name(employee.getLast_name());
        newEmployee.setEmail(employee.getEmail());
        newEmployee.setMobileNumber(employee.getMobileNumber());
        newEmployee.setDob(employee.getDob());
        newEmployee.setEmployeeStatus(EEmployeeStatus.ACTIVE); // Default status
        newEmployee.setRoles(employee.getRoles());
        newEmployee.setEmailVerified(true);

        newEmployee.setPassword(passwordEncoder.encode(employee.getPassword()));
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        return employeeRepository.save(newEmployee);
    }

    @Override
    public void deleteEmployee(UUID id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee updateEmployee(UUID id, EmployeeEditRequest employee) {
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setFirst_name(employee.getFirstName());
            existingEmployee.setLast_name(employee.getLastName());
            existingEmployee.setMobileNumber(employee.getPhoneNumber());
            existingEmployee.setDob(employee.getDob());
            existingEmployee.setEmployeeStatus(employee.getStatus());
            existingEmployee.setRoles(employee.getRoles());
            return employeeRepository.save(existingEmployee);
        }
        return null;
    }

}
