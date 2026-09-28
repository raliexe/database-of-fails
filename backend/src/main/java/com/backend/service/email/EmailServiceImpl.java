package com.backend.service.email;

import com.backend.entity.AppUser;
import com.backend.exception.NotFoundException;
import com.backend.exception.PersistenceException;
import com.backend.exception.ValidationException;
import com.backend.repository.user.UserRepository;
import com.backend.util.AuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${spring.mail.email}")
    private String sender;

    // To send a simple email
    @Transactional(rollbackFor = PersistenceException.class)
    public void sendForgotPasswordMail(String recipient) {
        AppUser appUser = null;
        try {
            log.info("Attempting to send forgot password mail to {}", recipient);
            appUser = userRepository.findAppUserByEmail(recipient);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
        if (appUser == null) {
            log.debug("NotFoundException: User not found!");
            throw new NotFoundException("User not found");
        }
        String token = UUID.randomUUID().toString();
        appUser.setToken(token);

        try {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

            // Creating a simple mail message
            String message = "<html><body><h2>Password Reset Link</h2>"
                + "Dear "
                + appUser.getNickname()
                + ",<br><br>"
                + "It seems you have forgotten your password. To reset your password, click the following link:<br><br>"
                + "<a href=\"http://localhost:4200/#/reset/"
                + token
                + "\""
                + ">Password Reset Link"
                + "</a><br><br>"
                + "<h4>Kind regards,</h4><h4>Database of Fails</h4></body></html>";

            // Setting up necessary details
            helper.setFrom(sender);
            helper.setTo(recipient);
            helper.setText(message, true);
            helper.setSubject("Reset Password Link");

            // Sending the mail
            javaMailSender.send(mailMessage);
            log.info("Forgot password mail sent to {}", recipient);
        } catch (MessagingException e) {
            log.debug("Sending of email failed", e);
        }
    }

    @Transactional(rollbackFor = PersistenceException.class)
    public void sendPasswordResetMail(Long id) {
        AppUser appUser;

        try {
            log.info("Attempting to send password reset mail to user with id:{}", id);
            appUser = userRepository.findAppUserById(id);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }

        if (appUser == null) {
            log.debug("NotFoundException: User not found!");
            throw new NotFoundException("User not found!");
        }
        Long loggedInUser = authenticationManager.getAuthenticatedUser().getId();
        if (Objects.equals(id, loggedInUser)) {
            log.debug("ValidationException: You cannot reset your own password!");
            throw new ValidationException("You cannot reset your own password!");
        }
        String password = UUID.randomUUID().toString().replace("-", "");

        try {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

            // Creating a simple mail message
            String message = "<html><body><h2>Password Reset</h2>"
                + "Dear "
                + appUser.getNickname()
                + ",<br><br>"
                + "Your password has been reset. Once signed in, we advise you to change the password as soon as possible. Your new password is:<br><br>"
                + "<h3>"
                + password
                + "</h3>"
                + "<h4>Kind regards,</h4><h4>Database of Fails</h4></body></html>";

            // Setting up necessary details
            helper.setFrom(sender);
            helper.setTo(appUser.getEmail());
            helper.setText(message, true);
            helper.setSubject("Password Reset");

            // Sending the mail
            javaMailSender.send(mailMessage);
            log.info("Reset password mail sent to {}", appUser.getEmail());
            appUser.setPassword(passwordEncoder.encode(password));
        } catch (MessagingException e) {
            log.debug("Sending of email failed", e);
        }
    }
}