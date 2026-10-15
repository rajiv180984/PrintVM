package com.first.auth.service;

import com.first.auth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Development stand-in that writes emails to the log instead of sending them.
 */
@Service
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "false", matchIfMissing = true)
public class LoggingEmailService implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(LoggingEmailService.class);

    @Override
    public void sendPasswordResetEmail(User user, String resetLink, Duration validFor) {
        log.info("[DEV MAIL] To: {} | Password reset link (valid {} min): {}",
                user.getEmail(), validFor.toMinutes(), resetLink);
    }

    @Override
    public void sendVerificationEmail(User user, String verificationLink, Duration validFor) {
        log.info("[DEV MAIL] To: {} | Email verification link (valid {} h): {}",
                user.getEmail(), validFor.toHours(), verificationLink);
    }

    @Override
    public void sendPasswordChangedEmail(User user) {
        log.info("[DEV MAIL] To: {} | Your password was changed", user.getEmail());
    }
}
