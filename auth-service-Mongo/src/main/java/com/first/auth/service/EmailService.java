package com.first.auth.service;

import com.first.auth.model.User;

import java.time.Duration;

/**
 * Outbound account notifications. {@link LoggingEmailService} is used unless
 * {@code app.mail.enabled=true}, in which case {@link SmtpEmailService} sends real mail.
 */
public interface EmailService {

    void sendPasswordResetEmail(User user, String resetLink, Duration validFor);

    void sendVerificationEmail(User user, String verificationLink, Duration validFor);

    void sendPasswordChangedEmail(User user);
}
