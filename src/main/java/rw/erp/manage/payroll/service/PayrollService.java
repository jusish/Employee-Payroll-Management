package rw.erp.manage.payroll.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rw.erp.manage.payroll.dto.response.ApiResponse;
import rw.erp.manage.payroll.enums.EEmployeeStatus;
import rw.erp.manage.payroll.enums.EEmploymentStatus;
import rw.erp.manage.payroll.enums.EPaymentStatus;
import rw.erp.manage.payroll.model.Deductions;
import rw.erp.manage.payroll.model.Employee;
import rw.erp.manage.payroll.model.Employment;
import rw.erp.manage.payroll.model.Payslip;
import rw.erp.manage.payroll.repository.DeductionsRepository;
import rw.erp.manage.payroll.repository.EmployeeRepository;
import rw.erp.manage.payroll.repository.EmploymentRepository;
import rw.erp.manage.payroll.repository.PayslipRepository;

import java.util.List;

@Service
public class PayrollService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmploymentRepository employmentRepository;
    @Autowired
    private DeductionsRepository deductionsRepository;
    @Autowired
    private PayslipRepository payslipRepository;

    public ApiResponse generatePayroll(Integer month, Integer year) {
        // Validate input
        if (month == null || year == null || month < 1 || month > 12 || year < 1900) {
            return new ApiResponse(false, "Invalid month or year provided.", null);
        }

        List<Employee> employees = employeeRepository.findByEmployeeStatus(EEmployeeStatus.ACTIVE);
        if (employees.isEmpty()) {
            return new ApiResponse(false, "No active employees found.", null);
        }

        int generatedCount = 0;
        StringBuilder errors = new StringBuilder();

        // Cache deductions to avoid repeated DB calls
        Deductions housingDed = deductionsRepository.findByDeductionName("Housing");
        Deductions transportDed = deductionsRepository.findByDeductionName("Transport");
        Deductions employeeTaxDed = deductionsRepository.findByDeductionName("Employee Tax");
        Deductions pensionDed = deductionsRepository.findByDeductionName("Pension");
        Deductions medicalDed = deductionsRepository.findByDeductionName("Medical Insurance");
        Deductions othersDed = deductionsRepository.findByDeductionName("Others");

        if (housingDed == null || transportDed == null || employeeTaxDed == null ||
                pensionDed == null || medicalDed == null || othersDed == null) {
            return new ApiResponse(false, "One or more deduction configurations are missing.", null);
        }

        for (Employee employee : employees) {
            Employment employment = employmentRepository.findByEmployeeAndStatus(employee, EEmploymentStatus.ACTIVE)
                    .orElse(null);
            if (employment == null) {
                errors.append("No active employment for employee: ").append(employee.getId()).append(". ");
                continue;
            }

            // Prevent duplicate payroll
            if (!payslipRepository.findByEmployeeIdAndMonthAndYear(employee.getId(), month, year).isEmpty()) {
                errors.append("Payslip already exists for employee: ").append(employee.getId()).append(". ");
                continue;
            }

            try {
                Payslip payslip = new Payslip();
                payslip.setEmployee(employee);
                payslip.setMonth(month);
                payslip.setYear(year);
                payslip.setStatus(EPaymentStatus.PENDING);

                Double baseSalary = employment.getBaseSalary();
                Double housing = baseSalary * (housingDed.getPercentage() / 100);
                Double transport = baseSalary * (transportDed.getPercentage() / 100);
                Double grossSalary = baseSalary + housing + transport;

                Double employeeTax = baseSalary * (employeeTaxDed.getPercentage() / 100);
                Double pension = baseSalary * (pensionDed.getPercentage() / 100);
                Double medical = baseSalary * (medicalDed.getPercentage() / 100);
                Double others = baseSalary * (othersDed.getPercentage() / 100);
                Double netSalary = grossSalary - (employeeTax + pension + medical + others);

                payslip.setHouseAmount(housing);
                payslip.setTransportAmount(transport);
                payslip.setEmployeeTaxedAmount(employeeTax);
                payslip.setPensionAmount(pension);
                payslip.setMedicalInsuranceAmount(medical);
                payslip.setOtherTaxedAmount(others);
                payslip.setGrossSalary(grossSalary);
                payslip.setNetSalary(netSalary);

                payslipRepository.save(payslip);
                generatedCount++;
            } catch (Exception ex) {
                errors.append("Error processing employee: ").append(employee.getId()).append(" - ")
                        .append(ex.getMessage()).append(". ");
            }
        }

        if (generatedCount == 0) {
            return new ApiResponse(false, "No payroll generated. " + errors.toString(), null);
        }
        String msg = "Payroll generated for " + generatedCount + " employees.";
        if (errors.length() > 0) {
            msg += " Some errors: " + errors.toString();
        }
        return new ApiResponse(true, msg, null);
    }
}