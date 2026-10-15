package com.first.auth.controller;

import com.first.auth.config.OpenApiConfig;
import com.first.auth.dto.AuthRequests.UpdateRolesRequest;
import com.first.auth.dto.AuthRequests.UpdateStatusRequest;
import com.first.auth.dto.AuthResponses.PageResponse;
import com.first.auth.dto.AuthResponses.UserResponse;
import com.first.auth.security.UserPrincipal;
import com.first.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "3. Admin", description = "User administration (requires ADMIN role)")
@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH)
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "List users (paginated)")
    public PageResponse<UserResponse> list(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return userService.listUsers(page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    public UserResponse get(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/roles")
    @Operation(summary = "Replace a user's roles (forces the user to log in again)")
    public UserResponse updateRoles(@AuthenticationPrincipal UserPrincipal principal, @PathVariable String id,
                                    @Valid @RequestBody UpdateRolesRequest request) {
        return userService.updateRoles(principal.id(), id, request.roles());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Enable or disable a user (disabling revokes all their tokens)")
    public UserResponse updateStatus(@AuthenticationPrincipal UserPrincipal principal, @PathVariable String id,
                                     @Valid @RequestBody UpdateStatusRequest request) {
        return userService.updateStatus(principal.id(), id, request.enabled());
    }

    @PostMapping("/{id}/unlock")
    @Operation(summary = "Clear a lockout caused by failed login attempts")
    public UserResponse unlock(@PathVariable String id) {
        return userService.unlock(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user and all of their tokens")
    public void delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable String id) {
        userService.deleteUser(principal.id(), id);
    }
}
