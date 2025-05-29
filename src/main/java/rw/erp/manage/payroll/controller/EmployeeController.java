package rw.erp.manage.payroll.controller;

import rw.erp.manage.payroll.dto.request.EmployeeEditRequest;
import rw.erp.manage.payroll.dto.request.RegisterRequest;
import rw.erp.manage.payroll.model.Employee;
import rw.erp.manage.payroll.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import rw.erp.manage.payroll.dto.response.ApiResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employee")
@Tag(name = "Employee", description = "Endpoints for managing employees")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> createEmployee(@RequestBody RegisterRequest employee) {
        Employee created = employeeService.createEmployee(employee);
        return ResponseEntity.ok(new ApiResponse(true, "Employee created successfully.", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateEmployee(@PathVariable UUID id,
            @RequestBody EmployeeEditRequest employee) {
        Employee updated = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(new ApiResponse(true, "Employee updated successfully.", updated));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(new ApiResponse(true, "Employees fetched successfully.", employees));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse> getEmployeeById(@PathVariable UUID id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Employee fetched successfully.", employee));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new ApiResponse(true, "Employee deleted successfully.", null));
    }
}