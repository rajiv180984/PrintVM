package com.first.auth.service;

import com.first.auth.config.AppProperties;
import com.first.auth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Sends plain-text mail through the SMTP server configured under {@code spring.mail.*}.
 */
@Service
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "true")
public class SmtpEmailService implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailService.class);

    private final JavaMailSender mailSender;
    private final String from;

    public SmtpEmailService(JavaMailSender mailSender, AppProperties props) {
        this.mailSender = mailSender;
        this.from = props.mail().from();
    }

    @Async
    @Override
    public void sendPasswordResetEmail(User user, String resetLink, Duration validFor) {
        send(user.getEmail(), "Reset your password",
                "Hello " + displayName(user) + ",\n\n"
                        + "We received a request to reset your password. Use the link below within "
                        + validFor.toMinutes() + " minutes:\n\n" + resetLink + "\n\n"
                        + "If you did not request this, you can ignore this email.");
    }

    @Async
    @Override
    public void sendVerificationEmail(User user, String verificationLink, Duration validFor) {
        send(user.getEmail(), "Verify your email address",
                "Hello " + displayName(user) + ",\n\n"
                        + "Please confirm your email address within " + validFor.toHours() + " hours:\n\n"
                        + verificationLink);
    }

    @Async
    @Override
    public void sendPasswordChangedEmail(User user) {
        send(user.getEmail(), "Your password was changed",
                "Hello " + displayName(user) + ",\n\n"
                        + "The password for your account was just changed. "
                        + "If this was not you, reset your password immediately and contact support.");
    }

    private void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to send '{}' email to {}", subject, to, e);
        }
    }

    private static String displayName(User user) {
        return user.getFirstName() != null ? user.getFirstName() : user.getUsername();
    }
}
