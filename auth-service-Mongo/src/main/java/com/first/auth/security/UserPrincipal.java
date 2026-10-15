package com.first.auth.security;

import com.first.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * Authenticated caller placed in the SecurityContext by {@link JwtAuthenticationFilter}.
 * Inject into controllers with {@code @AuthenticationPrincipal UserPrincipal principal}.
 */
public record UserPrincipal(String id, String username, List<GrantedAuthority> authorities) {

    public static UserPrincipal from(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + r.name()))
                .toList();
        return new UserPrincipal(user.getId(), user.getUsername(), authorities);
    }
}
