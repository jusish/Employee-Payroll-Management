package rw.erp.manage.payroll.model;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import rw.erp.manage.payroll.enums.EPaymentStatus;

@Data
@Entity
@Table(name = "payslip")
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Double houseAmount;
    private Double transportAmount;
    private Double employeeTaxedAmount;
    private Double pensionAmount;
    private Double medicalInsuranceAmount;
    private Double otherTaxedAmount;
    private Double grossSalary;
    private Double netSalary;
    @Positive
    private Integer month;
    private Integer year;

    @Enumerated(EnumType.STRING)
    private EPaymentStatus status;

}