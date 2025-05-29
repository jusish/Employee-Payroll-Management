package rw.erp.manage.payroll.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import rw.erp.manage.payroll.dto.response.ApiResponse;
import rw.erp.manage.payroll.enums.EPaymentStatus;
import rw.erp.manage.payroll.model.Payslip;
import rw.erp.manage.payroll.repository.PayslipRepository;
import rw.erp.manage.payroll.service.EmailService;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payslip")
@Tag(name = "Payslips", description = "Endpoints for managing payslips")
@SecurityRequirement(name = "bearerAuth")
public class PayslipController {

    @Autowired
    private PayslipRepository payslipRepository;

    @Autowired
    private EmailService emailService;

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> approvePayslip(@PathVariable UUID id) {
        Optional<Payslip> payslipOpt = payslipRepository.findById(id);
        if (payslipOpt.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(false, "Payslip not found.", null));
        }
        Payslip payslip = payslipOpt.get();
        if (payslip.getStatus() == EPaymentStatus.PAID) {
            return ResponseEntity.ok(new ApiResponse(false, "Payslip is already approved/paid.", payslip));
        }
        payslip.setStatus(EPaymentStatus.PAID);
        payslip = payslipRepository.save(payslip);

        // Send notification email directly
        String employeeEmail = payslip.getEmployee().getEmail();
        String employeeName = payslip.getEmployee().getFirst_name() + " " + payslip.getEmployee().getLast_name();
        emailService.sendPayslipPaidEmail(
                employeeEmail,
                employeeName,
                payslip.getMonth(),
                payslip.getYear(),
                payslip.getNetSalary());

        return ResponseEntity.ok(new ApiResponse(true, "Payslip approved and notification sent.", payslip));
    }
}