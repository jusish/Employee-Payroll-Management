package rw.erp.manage.payroll.service;

import jakarta.annotation.PostConstruct;
import rw.erp.manage.payroll.model.Deductions;
import rw.erp.manage.payroll.repository.DeductionsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeductionsService {

    @Autowired
    private DeductionsRepository deductionsRepository;

    @PostConstruct
    public void initDeductions() {
        String[][] defaultDeductions = {
                { "Employee Tax", "30.0" },
                { "Pension", "6.0" },
                { "Medical Insurance", "5.0" },
                { "Others", "5.0" },
                { "Housing", "14.0" },
                { "Transport", "14.0" }
        };

        for (String[] deduction : defaultDeductions) {
            if (deductionsRepository.findByDeductionName(deduction[0]) == null) {
                Deductions d = new Deductions();
                d.setDeductionName(deduction[0]);
                d.setPercentage(Double.parseDouble(deduction[1]));
                deductionsRepository.save(d);
            }
        }
    }
}