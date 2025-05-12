package com.example.forum.service;

import com.example.forum.entity.User;
import com.example.forum.repository.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
// import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
// import java.util.ArrayList;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MailService mailService;

    public UserService(UserRepository userRepository, @Lazy MailService mailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String password, String email) {
        String hashedPassword = hashPassword(password);
        User user = new User(username, hashedPassword, email);
        user.setOtp(generateOtp());
        user = userRepository.save(user);
        mailService.sendOtpEmail(user.getEmail(), user.getOtp());
        return user;
    }

    public boolean verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        if (user.getOtp().equals(otp)) {
            user.setVerified(true);
            user.setOtp(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public User loginUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        if (!user.isVerified()) {
            throw new UsernameNotFoundException("User not verified: " + email);
        }

        return user;

    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    private String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(1_000_000);
        return String.format("%06d", otp);
    }
}
