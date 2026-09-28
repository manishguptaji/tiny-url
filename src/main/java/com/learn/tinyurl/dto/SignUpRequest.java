package com.learn.tinyurl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "email is req.")
        @Email(message = "email is not valid.")
        String email,

        @NotBlank(message = "password is req.")
        @Size(min = 6, message = "password must be at least 6 characters.")
        String password
) {
}
