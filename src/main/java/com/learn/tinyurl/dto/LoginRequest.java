package com.learn.tinyurl.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "email is req.")
        @Email(message = "email is not valid.")
        String email,

        @NotBlank(message = "password is req.")
        String password
) {
}
