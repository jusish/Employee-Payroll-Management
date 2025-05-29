package rw.erp.manage.payroll.model;

import jakarta.persistence.*;
import lombok.Data;
import rw.erp.manage.payroll.enums.EEmploymentStatus;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "employment")
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID code;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private String department;
    private String position;
    private Double baseSalary;

    @Enumerated(EnumType.STRING)
    private EEmploymentStatus status;

    private LocalDate joiningDate;

}