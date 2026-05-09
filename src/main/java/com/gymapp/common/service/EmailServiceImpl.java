package com.gymapp.common.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@gymapp.com}")
    private String fromAddress;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("GymApp - Reset your password");
            helper.setText(buildEmailBody(resetLink), true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException | MailException ex) {
            // Log the error but do not propagate - we never want to leak whether
            // a given email address exists in our system via an error response.
            org.slf4j.LoggerFactory.getLogger(EmailServiceImpl.class)
                    .error("Failed to send password-reset email to {}: {}", toEmail, ex.getMessage());
        }
    }

    private String buildEmailBody(String resetLink) {
        return """
                <html>
                  <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2>Reset your GymApp password</h2>
                    <p>We received a request to reset the password for your account.</p>
                    <p>Click the button below to choose a new password. This link expires in <strong>30 minutes</strong>.</p>
                    <p style="margin: 24px 0;">
                      <a href="%s"
                         style="background:#2563eb;color:#fff;padding:12px 24px;border-radius:6px;text-decoration:none;font-weight:bold;">
                        Reset Password
                      </a>
                    </p>
                    <p>If the button doesn't work, copy and paste this URL into your browser:</p>
                    <p><a href="%s">%s</a></p>
                    <hr/>
                    <p style="font-size:12px;color:#888;">
                      If you didn't request a password reset, you can safely ignore this email.
                      Your password will not change.
                    </p>
                  </body>
                </html>
                """.formatted(resetLink, resetLink, resetLink);
    }
}



