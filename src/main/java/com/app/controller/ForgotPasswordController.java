package com.app.controller;

import com.app.dto.ForgotPasswordRequest;
import com.app.dto.ResponseMessage;
import com.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")

public class ForgotPasswordController {

    @Autowired
    private AuthService authService;

    // Endpoint for Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseMessage> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail(); // Extract email from the request object
        boolean isSuccess = authService.forgotPassword(email);

        if (isSuccess) {
            return ResponseEntity.ok(new ResponseMessage("Password reset link has been sent to your email."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new ResponseMessage("Error: Could not send password reset link. Please try again later."));
        }
    }
   


    // Endpoint for Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            boolean isResetSuccess = authService.resetPassword(token, newPassword);
            if (isResetSuccess) {
                return ResponseEntity.ok(new ResponseMessage("Your password has been successfully reset."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body(new ResponseMessage("Error: Invalid token or failed to reset password. Please try again."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ResponseMessage("Error resetting password: " + e.getMessage()));
        }
    }

}
