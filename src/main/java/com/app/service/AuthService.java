package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.app.Repository.PasswordResetTokenRepository;
import com.app.Repository.UserRepository;
import com.app.entity.PasswordResetToken;
import com.app.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    // Register a new user
    public void register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists!");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists!");
        }
        userRepository.save(user);
    }

    // Authenticate user during login
    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && password.equals(user.getPassword());
    }

    // Forgot Password - Generate Reset Link
    public boolean forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false; // Email not found
        }

        // Delete any existing tokens for this user
        tokenRepository.deleteByUser(user);

        // Generate a new unique token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(user, token, LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);

        // Create and send the reset link
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        sendResetPasswordEmail(user.getEmail(), resetLink);

        return true; // Successfully sent reset link
    }


    // Send Email
    private void sendResetPasswordEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink);
        emailService.sendEmail(message);
    }

    // Reset Password using the token
    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            throw new RuntimeException("Invalid or expired token!");
        }

        // Reset the user's password
        User user = resetToken.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);

        // Delete the token after use to prevent reuse
        tokenRepository.delete(resetToken);
        return true;
    }

}
