package com.example.forum.controller;

import com.example.forum.dto.LoginRequest;
import com.example.forum.dto.LoginResponse;
import com.example.forum.dto.RegisterRequest;
import com.example.forum.entity.User;
import com.example.forum.service.JwtService;
import com.example.forum.service.UserService;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        // this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = userService.registerUser(request.getUsername(), request.getPassword(), request.getEmail());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UserDetails user = userService.loginUserByEmail(request.getEmail());
        if (userService.verifyPassword(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(request.getEmail());
            Long userId = userService.findUserIdByEmail(request.getEmail());

            return ResponseEntity.ok(new LoginResponse(userId, token));
        }

        return ResponseEntity.status(401).body(new LoginResponse(null, "Invalid credentials"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean verified = userService.verifyOtp(email, otp);
        if (verified) {
            return ResponseEntity.ok("Email verified successfully");
        }
        return ResponseEntity.status(400).body("Invalid OTP");
    }
}