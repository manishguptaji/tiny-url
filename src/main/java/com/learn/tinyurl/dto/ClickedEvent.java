package com.learn.tinyurl.dto;


public record ClickedEvent(
        String shortCode,
        String clickedAt,
        String referrer,
        String userAgent
) {
}
