package rw.erp.manage.payroll.service;

import rw.erp.manage.payroll.dto.request.RegisterRequest;
import rw.erp.manage.payroll.model.Employee;

public interface UserService {
    Employee registerUser(RegisterRequest registerRequest);

    Employee findByEmail(String email);

    void verifyEmail(String email, String code);

    void sendForgotPasswordCode(String email);

    void resetPassword(String email, String code, String newPassword);
}
