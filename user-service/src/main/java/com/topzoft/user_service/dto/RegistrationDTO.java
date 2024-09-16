package com.topzoft.user_service.dto;

import com.topzoft.user_service.enums.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {

    @Schema(description = "The user's full name", example = "John Doe", required = true)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
    private String name;

    @Schema(description = "The user's email address", example = "john.doe@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "The user's password", example = "password123", minLength = 8, required = true)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Schema(description = "The user's phone number", example = "+1234567890", required = false)
    private String phoneNumber;

    @Schema(description = "The user's address", example = "1234 Elm Street, Springfield", required = false)
    private String address;

    @Schema(description = "The role of the user", example = "ADMIN", required = true)
    @NotNull(message = "Role is required")
    private Role role; // This could be an enum in the model (CUSTOMER, ADMIN, etc.)
    
    // Getters and Setters
}
