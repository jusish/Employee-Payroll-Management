package rw.erp.manage.payroll.service;

import rw.erp.manage.payroll.dto.request.EmploymentRequest;
import rw.erp.manage.payroll.dto.response.EmploymentResponse;

import java.util.List;
import java.util.UUID;

public interface EmploymentService {
    EmploymentResponse createEmployment(EmploymentRequest request);

    EmploymentResponse getEmploymentById(UUID id);

    List<EmploymentResponse> getAllEmployments();

    EmploymentResponse updateEmployment(UUID id, EmploymentRequest request);

    void deleteEmployment(UUID id);
}