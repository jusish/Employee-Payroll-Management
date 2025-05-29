package rw.erp.manage.payroll.serviceImpl;

import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rw.erp.manage.payroll.dto.request.RegisterRequest;
import rw.erp.manage.payroll.enums.EEmployeeStatus;
import rw.erp.manage.payroll.enums.ERole;
import rw.erp.manage.payroll.exception.DuplicateEmailException;
import rw.erp.manage.payroll.exception.ResourceNotFoundException;
import rw.erp.manage.payroll.model.Employee;
import rw.erp.manage.payroll.repository.UserRepository;
import rw.erp.manage.payroll.service.EmailService;
import rw.erp.manage.payroll.service.UserService;
import rw.erp.manage.payroll.util.LoggerUtil;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerUtil.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    private String generate6DigitCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Override
    public Employee registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            logger.warn("Registration failed: Email {} already in use", registerRequest.getEmail());
            throw new DuplicateEmailException("Email already in use");
        }
        Employee user = new Employee();
        user.setFirst_name(registerRequest.getFirst_name());
        user.setLast_name(registerRequest.getLast_name());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Set<ERole> roles = registerRequest.getRoles() != null ? registerRequest.getRoles()
                : Collections.singleton(ERole.EMPLOYEE);
        user.setRoles(roles);
        user.setMobileNumber(registerRequest.getMobileNumber());
        user.setDob(registerRequest.getDob());
        // Set default status to active
        user.setEmployeeStatus(EEmployeeStatus.ACTIVE);
        user.setEmailVerified(false);

        String verificationCode = generate6DigitCode();
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusHours(24));

        Employee savedUser = userRepository.save(user);
        emailService.sendVerificationEmail(savedUser.getEmail(), verificationCode);

        logger.info("User registered: {}", savedUser.getEmail());
        return savedUser;
    }

    @Override
    public Employee findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public void verifyEmail(String email, String code) {
        Employee user = userRepository.findByEmail(email)
                .filter(u -> code.equals(u.getVerificationCode())
                        && u.getVerificationCodeExpiry().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired verification code"));

        user.setEmailVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);
        logger.info("Email verified for user: {}", user.getEmail());
    }

    @Override
    public void sendForgotPasswordCode(String email) {
        Employee user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        String code = generate6DigitCode();
        user.setResetPasswordCode(code);
        user.setResetPasswordCodeExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
        emailService.sendResetPasswordEmail(email, code);
        logger.info("Sent reset password code to: {}", email);
    }

    @Override
    public void resetPassword(String email, String code, String newPassword) {
        Employee user = userRepository.findByEmail(email)
                .filter(u -> code.equals(u.getResetPasswordCode())
                        && u.getResetPasswordCodeExpiry().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired reset code"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordCode(null);
        user.setResetPasswordCodeExpiry(null);
        userRepository.save(user);
        logger.info("Password reset for user: {}", email);
    }
}