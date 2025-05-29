package rw.erp.manage.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rw.erp.manage.payroll.dto.request.EmploymentRequest;
import rw.erp.manage.payroll.dto.response.EmploymentResponse;
import rw.erp.manage.payroll.dto.response.ApiResponse;
import rw.erp.manage.payroll.service.EmploymentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employment")
public class EmploymentController {

    @Autowired
    private EmploymentService employmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> createEmployment(@RequestBody EmploymentRequest request) {
        EmploymentResponse created = employmentService.createEmployment(request);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Invalid employee ID or input data.", null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Employment created successfully.", created));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getEmploymentById(@PathVariable UUID id) {
        EmploymentResponse employment = employmentService.getEmploymentById(id);
        if (employment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Employment not found with the provided ID.", null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Employment fetched successfully.", employment));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getAllEmployments() {
        List<EmploymentResponse> employments = employmentService.getAllEmployments();
        if (employments.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(true, "No employments found.", employments));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Employments fetched successfully.", employments));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> updateEmployment(@PathVariable UUID id, @RequestBody EmploymentRequest request) {
        EmploymentResponse updated = employmentService.updateEmployment(id, request);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Employment not found or invalid input.", null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Employment updated successfully.", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteEmployment(@PathVariable UUID id) {
        EmploymentResponse employment = employmentService.getEmploymentById(id);
        if (employment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Employment not found with the provided ID.", null));
        }
        employmentService.deleteEmployment(id);
        return ResponseEntity.ok(new ApiResponse(true, "Employment deleted successfully.", null));
    }
}