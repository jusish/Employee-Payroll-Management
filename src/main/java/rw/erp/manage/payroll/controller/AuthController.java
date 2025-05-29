package rw.erp.manage.payroll.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import rw.erp.manage.payroll.dto.request.LoginRequest;
import rw.erp.manage.payroll.dto.request.RegisterRequest;
import rw.erp.manage.payroll.dto.response.ApiResponse;
import rw.erp.manage.payroll.dto.response.AuthResponse;
import rw.erp.manage.payroll.exception.EmailNotVerifiedException;
import rw.erp.manage.payroll.exception.InvalidCredentialsException;
import rw.erp.manage.payroll.model.Employee;
import rw.erp.manage.payroll.security.JwtTokenUtil;
import rw.erp.manage.payroll.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
            JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest registerRequest) {
        userService.registerUser(registerRequest);
        return ResponseEntity
                .ok(new ApiResponse(true, "User registered successfully. Please verify your email.", null));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Employee user = userService.findByEmail(loginRequest.getEmail());
            if (!user.isEmailVerified()) {
                throw new EmailNotVerifiedException("Please verify your email before logging in.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateAccessToken(user);
            return ResponseEntity.ok(new AuthResponse(token, "Bearer"));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email) {
        userService.sendForgotPasswordCode(email);
        return ResponseEntity.ok(new ApiResponse(true, "Reset code sent to your email.", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String email,
            @RequestParam String code,
            @RequestParam String newPassword) {
        userService.resetPassword(email, code, newPassword);
        return ResponseEntity.ok(new ApiResponse(true, "Password reset successful.", null));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("email") String email,
            @RequestParam("code") String code) {
        userService.verifyEmail(email, code);
        return ResponseEntity.ok(new ApiResponse(true, "Email verified successfully", null));
    }
}
