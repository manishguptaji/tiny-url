package com.learn.tinyurl.dto;

public record CreateUrlResponse(
    String shortUrl,
    String longUrl,
    String customAlias
) {
}
