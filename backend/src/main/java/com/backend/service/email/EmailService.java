package com.backend.service.email;

public interface EmailService {

    /**
     * Send forgot password email to user with given email address.
     *
     * @param recipient - the email address of the recipient, whom the email will be to be sent
     */
    void sendForgotPasswordMail(String recipient);

    /**
     * Send password reset email to user with given id.
     *
     * @param id - id of the user, whom the email will be to be sent
     */
    void sendPasswordResetMail(Long id);
}