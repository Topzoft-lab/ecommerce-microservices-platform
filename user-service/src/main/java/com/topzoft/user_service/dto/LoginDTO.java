package com.topzoft.user_service.dto;

import lombok.Getter;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class LoginDTO {

    @Schema(description = "User's email", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "User's password", example = "password123", required = true, minLength = 6)
    private String password;

    // Getters and Setters
}
