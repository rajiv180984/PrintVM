package com.first.auth.config;

import com.first.auth.model.Role;
import com.first.auth.model.User;
import com.first.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

/**
 * Inserts sample users on startup when {@code app.seed.enabled=true} (default for local dev).
 * Existing users are left untouched, so restarts are safe.
 *
 * <pre>
 *   admin       / Admin@12345  (ADMIN, USER)
 *   john.doe    / User@12345   (USER)
 *   jane.smith  / User@12345   (USER)
 *   disabled.user / User@12345   (USER, disabled - for negative tests)
 * </pre>
 */
@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties props;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder, AppProperties props) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.props = props;
    }

    @Override
    public void run(String... args) {
        String adminPw = props.seed().adminPassword();
        String userPw = props.seed().userPassword();
        seed("admin", "admin@example.com", adminPw, "System", "Admin", Set.of(Role.ADMIN, Role.USER), true);
        seed("john.doe", "john.doe@example.com", userPw, "John", "Doe", Set.of(Role.USER), true);
        seed("jane.smith", "jane.smith@example.com", userPw, "Jane", "Smith", Set.of(Role.USER), true);
        seed("disabled.user", "disabled.user@example.com", userPw, "Disabled", "User", Set.of(Role.USER), false);
    }

    private void seed(String username, String email, String password, String first, String last,
                      Set<Role> roles, boolean enabled) {
        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(first);
        user.setLastName(last);
        user.setRoles(roles);
        user.setEnabled(enabled);
        user.setEmailVerified(true);
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
        log.info("Seeded user '{}'", username);
    }
}
