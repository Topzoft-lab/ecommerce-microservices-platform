package com.topzoft.user_service.controller;

import com.topzoft.user_service.dto.RegistrationDTO;
import com.topzoft.user_service.model.User;
import com.topzoft.user_service.dto.LoginDTO;
import com.topzoft.user_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // User registration

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user by providing their details like name, email, and password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "Bad request, user already exists or invalid input", 
                     content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        try {
            User registeredUser = userService.registerUser(registrationDTO);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // User login
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates a user using their email and password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User logged in successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid credentials", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            User loggedInUser = userService.loginUser(loginDTO);
            System.out.println(loggedInUser.toString());
            return ResponseEntity.ok("User logged in successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
 