package com.gymapp.common.service;

public interface EmailService {

    /**
     * Sends a password-reset email containing a one-time link.
     *
     * @param toEmail   recipient address
     * @param resetLink full URL the user should click to reset their password
     */
    void sendPasswordResetEmail(String toEmail, String resetLink);
}


