package com.learn.tinyurl.dto;

import java.time.LocalDateTime;

public record CreateUrlResponse(
    String shortUrl,
    String longUrl,
    String customAlias,
    LocalDateTime expiresAt
) {
}
