package rw.erp.manage.payroll.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import rw.erp.manage.payroll.dto.response.ApiResponse;
import rw.erp.manage.payroll.model.Payslip;
import rw.erp.manage.payroll.repository.PayslipRepository;
import rw.erp.manage.payroll.service.PayrollService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payroll")
@Tag(name = "Payroll", description = "Endpoints for managing payroll and payslips")
@SecurityRequirement(name = "bearerAuth")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private PayslipRepository payslipRepository;

    @PostMapping("/generate/{month}/{year}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> generatePayroll(@PathVariable Integer month, @PathVariable Integer year) {
        try {
            ApiResponse response = payrollService.generatePayroll(month, year);
            return ResponseEntity.ok(new ApiResponse(true, "Payroll generated successfully.", response.getData()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse(false, "Error generating payroll: " + e.getMessage(), null));
        }

    }

    @GetMapping("/payslip/{employeeId}/{month}/{year}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getPayslip(@PathVariable UUID employeeId, @PathVariable Integer month,
            @PathVariable Integer year) {
        List<Payslip> payslips = payslipRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year);
        if (payslips.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(false, "No payslips found for this employee and period.", null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Payslips fetched successfully.", payslips));
    }

    @GetMapping("/payslip/{month}/{year}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApiResponse> getAllPayslips(@PathVariable Integer month, @PathVariable Integer year) {
        List<Payslip> payslips = payslipRepository.findByMonthAndYear(month, year);
        if (payslips.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(false, "No payslips found for this period.", null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Payslips fetched successfully.", payslips));
    }
}