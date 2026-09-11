package com.learn.tinyurl.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUrlRequest(
    @NotBlank(message = "Long URL cannot be blank")
    @Size(max = 2048, message = "Long URL cannot exceed 2048 characters")
    @Pattern(regexp = "^(https?://).+", message = "Long URL must start with http:// or https://")
    String longUrl,

    @Size(max = 30, message = "Custom alias cannot exceed 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Custom alias can only contain alphanumeric characters, hyphens, and underscores")
    String customAlias
) {
}