package rw.erp.manage.payroll.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "deductions")
public class Deductions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID code;

    @Column(unique = true, nullable = false)
    private String deductionName;

    private Double percentage;
}