package rw.erp.manage.payroll.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import rw.erp.manage.payroll.dto.response.ApiResponse;
import rw.erp.manage.payroll.model.Deductions;
import rw.erp.manage.payroll.repository.DeductionsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deductions")
@Tag(name = "Deductions", description = "Endpoints for managing deductions")
@SecurityRequirement(name = "bearerAuth")
public class DeductionsController {

    @Autowired
    private DeductionsRepository deductionsRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getAllDeductions() {
        List<Deductions> list = deductionsRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Deductions fetched successfully.", list));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createDeduction(@RequestBody Deductions deduction) {
        Deductions saved = deductionsRepository.save(deduction);
        return ResponseEntity.ok(new ApiResponse(true, "Deduction created successfully.", saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateDeduction(@PathVariable UUID id, @RequestBody Deductions deduction) {
        Optional<Deductions> existing = deductionsRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(false, "Deduction not found.", null));
        }
        deduction.setCode(id);
        Deductions updated = deductionsRepository.save(deduction);
        return ResponseEntity.ok(new ApiResponse(true, "Deduction updated successfully.", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteDeduction(@PathVariable UUID id) {
        Optional<Deductions> existing = deductionsRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(false, "Deduction not found.", null));
        }
        deductionsRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Deduction deleted successfully.", null));
    }
}