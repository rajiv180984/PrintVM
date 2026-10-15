package com.first.auth.controller;

import com.first.auth.dto.AuthRequests.EmailRequest;
import com.first.auth.dto.AuthRequests.LoginRequest;
import com.first.auth.dto.AuthRequests.RefreshTokenRequest;
import com.first.auth.dto.AuthRequests.RegisterRequest;
import com.first.auth.dto.AuthRequests.ResetPasswordRequest;
import com.first.auth.dto.AuthRequests.ValidateTokenRequest;
import com.first.auth.dto.AuthResponses.ErrorResponse;
import com.first.auth.dto.AuthResponses.MessageResponse;
import com.first.auth.dto.AuthResponses.RegistrationResponse;
import com.first.auth.dto.AuthResponses.TokenResponse;
import com.first.auth.dto.AuthResponses.TokenValidationResponse;
import com.first.auth.service.AuthService;
import com.first.auth.service.ClientInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "1. Authentication", description = "Public endpoints: register, login, tokens, password recovery")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user account", responses = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Username or email already in use",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public RegistrationResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate and obtain an access token + refresh token", responses = {
            @ApiResponse(responseCode = "200", description = "Authenticated"),
            @ApiResponse(responseCode = "401", description = "Bad credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Account disabled or email not verified",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "423", description = "Account temporarily locked",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public TokenResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest http) {
        return authService.login(request, ClientInfo.from(http));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Exchange a refresh token for a new token pair (the old refresh token is invalidated)")
    public TokenResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request, HttpServletRequest http) {
        return authService.refresh(request.refreshToken(), ClientInfo.from(http));
    }

    @PostMapping("/logout")
    @Operation(summary = "Revoke a refresh token (logout of the current session)")
    public MessageResponse logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.refreshToken());
        return new MessageResponse("Logged out");
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Send a password reset link to the given email",
            description = "Always returns 200 to avoid revealing which emails are registered. "
                    + "In dev (app.security.expose-tokens-in-response=true) the reset token is returned as debugToken.")
    public MessageResponse forgotPassword(@Valid @RequestBody EmailRequest request) {
        return authService.forgotPassword(request.email());
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Set a new password using the token from the reset email",
            description = "Signs the user out of all sessions.")
    public MessageResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return new MessageResponse("Password has been reset. Please log in with your new password.");
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Confirm an email address using the token from the verification email")
    public MessageResponse verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return new MessageResponse("Email verified successfully");
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Send a new email verification link")
    public MessageResponse resendVerification(@Valid @RequestBody EmailRequest request) {
        return authService.resendVerification(request.email());
    }

    @PostMapping("/validate-token")
    @Operation(summary = "Introspect an access token",
            description = "For other services: checks signature, expiry and revocation and returns the token's claims.")
    public TokenValidationResponse validateToken(@Valid @RequestBody ValidateTokenRequest request) {
        return authService.validateToken(request.token());
    }
}
