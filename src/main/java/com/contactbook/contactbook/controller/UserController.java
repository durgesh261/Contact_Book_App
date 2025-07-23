package com.contactbook.contactbook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            // This case is handled by the exceptionEntryPoint, but as a safeguard:
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }
        // Return a simple map with the username
        return ResponseEntity.ok(Map.of("username", userDetails.getUsername()));
    }
}
