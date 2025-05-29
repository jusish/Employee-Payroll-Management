package rw.erp.manage.payroll.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rw.erp.manage.payroll.dto.request.EmploymentRequest;
import rw.erp.manage.payroll.dto.response.EmploymentResponse;
import rw.erp.manage.payroll.model.Employee;
import rw.erp.manage.payroll.model.Employment;
import rw.erp.manage.payroll.repository.EmployeeRepository;
import rw.erp.manage.payroll.repository.EmploymentRepository;
import rw.erp.manage.payroll.service.EmploymentService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmploymentServiceImpl implements EmploymentService {

    @Autowired
    private EmploymentRepository employmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private EmploymentResponse toResponse(Employment employment) {
        EmploymentResponse resp = new EmploymentResponse();
        resp.setCode(employment.getCode());
        resp.setEmployeeId(employment.getEmployee().getId());
        resp.setDepartment(employment.getDepartment());
        resp.setPosition(employment.getPosition());
        resp.setBaseSalary(employment.getBaseSalary());
        resp.setStatus(employment.getStatus());
        resp.setJoiningDate(employment.getJoiningDate());
        return resp;
    }

    @Override
    public EmploymentResponse createEmployment(EmploymentRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElse(null);
        if (employee == null)
            return null;
        Employment employment = new Employment();
        employment.setEmployee(employee);
        employment.setDepartment(request.getDepartment());
        employment.setPosition(request.getPosition());
        employment.setBaseSalary(request.getBaseSalary());
        employment.setStatus(request.getStatus());
        employment.setJoiningDate(request.getJoiningDate());
        return toResponse(employmentRepository.save(employment));
    }

    @Override
    public EmploymentResponse getEmploymentById(UUID id) {
        Optional<Employment> employment = employmentRepository.findById(id);
        return employment.map(this::toResponse).orElse(null);
    }

    @Override
    public List<EmploymentResponse> getAllEmployments() {
        return employmentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public EmploymentResponse updateEmployment(UUID id, EmploymentRequest request) {
        Optional<Employment> optionalEmployment = employmentRepository.findById(id);
        if (optionalEmployment.isEmpty())
            return null;
        Employment employment = optionalEmployment.get();
        if (request.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(request.getEmployeeId()).orElse(null);
            if (employee != null)
                employment.setEmployee(employee);
        }
        employment.setDepartment(request.getDepartment());
        employment.setPosition(request.getPosition());
        employment.setBaseSalary(request.getBaseSalary());
        employment.setStatus(request.getStatus());
        employment.setJoiningDate(request.getJoiningDate());
        return toResponse(employmentRepository.save(employment));
    }

    @Override
    public void deleteEmployment(UUID id) {
        employmentRepository.deleteById(id);
    }
}