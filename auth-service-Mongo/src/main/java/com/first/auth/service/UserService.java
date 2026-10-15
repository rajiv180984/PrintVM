package com.first.auth.service;

import com.first.auth.dto.AuthRequests.UpdateProfileRequest;
import com.first.auth.dto.AuthResponses.PageResponse;
import com.first.auth.dto.AuthResponses.UserResponse;
import com.first.auth.exception.ApiException;
import com.first.auth.model.Role;
import com.first.auth.model.User;
import com.first.auth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Profile management for the current user and account administration for admins.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final OneTimeTokenService oneTimeTokenService;

    public UserService(UserRepository userRepository, RefreshTokenService refreshTokenService,
                       OneTimeTokenService oneTimeTokenService) {
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.oneTimeTokenService = oneTimeTokenService;
    }

    public UserResponse getUser(String id) {
        return UserResponse.from(findUser(id));
    }

    public UserResponse updateProfile(String id, UpdateProfileRequest req) {
        User user = findUser(id);
        if (req.firstName() != null) {
            user.setFirstName(req.firstName().trim());
        }
        if (req.lastName() != null) {
            user.setLastName(req.lastName().trim());
        }
        return UserResponse.from(userRepository.save(user));
    }

    public PageResponse<UserResponse> listUsers(int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), 100);
        Page<User> result = userRepository.findAll(
                PageRequest.of(Math.max(page, 0), safeSize, Sort.by(Sort.Direction.ASC, "createdAt")));
        return new PageResponse<>(result.map(UserResponse::from).getContent(), result.getNumber(),
                result.getSize(), result.getTotalElements(), result.getTotalPages());
    }

    public UserResponse updateRoles(String actingUserId, String targetId, Set<Role> roles) {
        if (actingUserId.equals(targetId) && !roles.contains(Role.ADMIN)) {
            throw ApiException.badRequest("You cannot remove your own ADMIN role");
        }
        User user = findUser(targetId);
        user.setRoles(new HashSet<>(roles));
        user.incrementTokenVersion(); // force re-login so new tokens carry the new roles
        refreshTokenService.revokeAllForUser(targetId);
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse updateStatus(String actingUserId, String targetId, boolean enabled) {
        if (actingUserId.equals(targetId) && !enabled) {
            throw ApiException.badRequest("You cannot disable your own account");
        }
        User user = findUser(targetId);
        user.setEnabled(enabled);
        if (!enabled) {
            user.incrementTokenVersion();
            refreshTokenService.revokeAllForUser(targetId);
        }
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse unlock(String targetId) {
        User user = findUser(targetId);
        user.setLockedUntil(null);
        user.setFailedLoginAttempts(0);
        return UserResponse.from(userRepository.save(user));
    }

    public void deleteUser(String actingUserId, String targetId) {
        if (actingUserId.equals(targetId)) {
            throw ApiException.badRequest("You cannot delete your own account");
        }
        User user = findUser(targetId);
        refreshTokenService.deleteAllForUser(targetId);
        oneTimeTokenService.deleteAllForUser(targetId);
        userRepository.delete(user);
    }

    private User findUser(String id) {
        return userRepository.findById(id).orElseThrow(() -> ApiException.notFound("User not found"));
    }
}
