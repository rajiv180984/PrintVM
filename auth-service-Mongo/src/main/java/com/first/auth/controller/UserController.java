package com.first.auth.controller;

import com.first.auth.config.OpenApiConfig;
import com.first.auth.dto.AuthRequests.ChangePasswordRequest;
import com.first.auth.dto.AuthRequests.UpdateProfileRequest;
import com.first.auth.dto.AuthResponses.MessageResponse;
import com.first.auth.dto.AuthResponses.UserResponse;
import com.first.auth.security.UserPrincipal;
import com.first.auth.service.AuthService;
import com.first.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me")
@Tag(name = "2. Current user", description = "Endpoints for the authenticated user")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "Get the current user's profile")
    public UserResponse me(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.getUser(principal.id());
    }

    @PutMapping
    @Operation(summary = "Update the current user's profile")
    public UserResponse updateProfile(@AuthenticationPrincipal UserPrincipal principal,
                                      @Valid @RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(principal.id(), request);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password (requires the current password; signs out all sessions)")
    public MessageResponse changePassword(@AuthenticationPrincipal UserPrincipal principal,
                                          @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(principal.id(), request);
        return new MessageResponse("Password changed. Please log in again.");
    }

    @PostMapping("/logout-all")
    @Operation(summary = "Sign out of every session and invalidate all issued tokens")
    public MessageResponse logoutAll(@AuthenticationPrincipal UserPrincipal principal) {
        authService.logoutAll(principal.id());
        return new MessageResponse("Logged out of all sessions");
    }
}
