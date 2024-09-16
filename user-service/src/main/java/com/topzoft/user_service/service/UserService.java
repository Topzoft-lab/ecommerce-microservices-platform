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
public class UserService  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(RegistrationDTO registrationDTO) {

        // Check if email is already in use
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        // Create a new User object from the DTO
        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword())); // Hash the password
        
        // Set default role as CUSTOMER
        user.setRole(registrationDTO.getRole());

        // Set optional fields like phone number and address if provided
        if (registrationDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(registrationDTO.getPhoneNumber());
        }
        
        if (registrationDTO.getAddress() != null) {
            user.setAddress(registrationDTO.getAddress());
        }

        // Save the new user in the repository
        return userRepository.save(user);
    }

    
    public User loginUser(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());

        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials!");
        }

        return user;
    }

}
