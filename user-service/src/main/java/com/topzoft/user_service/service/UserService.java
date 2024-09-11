package com.topzoft.user_service.service;

import com.topzoft.user_service.dto.RegistrationDTO;
import com.topzoft.user_service.dto.LoginDTO;
import com.topzoft.user_service.model.User;
import com.topzoft.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<String> registerUser(RegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword())); // Hash the password

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    public ResponseEntity<String> loginUser(LoginDTO loginDTO) {
        // User user = userRepository.findByEmail(loginDTO.getEmail());

        // if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
        //     return ResponseEntity.badRequest().body("Invalid credentials!");
        // }

        // // Normally, you would generate a JWT token here
        return ResponseEntity.ok("User logged in successfully!");
    }
}
