package com.ems.employee_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(Authentication authentication) {
        // Because our JWT filter ran successfully, Spring Security knows who the user is.
        // We can get their details directly from the 'Authentication' object.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        return ResponseEntity.ok("This is the profile for user: " + userEmail);
    }
}