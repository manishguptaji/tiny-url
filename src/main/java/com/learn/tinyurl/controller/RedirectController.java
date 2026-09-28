package com.learn.tinyurl.controller;

import com.learn.tinyurl.service.ClickService;
import com.learn.tinyurl.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RedirectController {
    private final UrlService urlService;
    private final ClickService clickService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable String shortUrl,
                                                  @RequestHeader(value = "Referer", required = false) String referrer,
                                                  @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        String longUrl = urlService.getLongUrl(shortUrl);
        // Redirect to the long URL
        if (longUrl == null) {
            return ResponseEntity.notFound().build();
        }
        // Record the click
        clickService.recordClick(shortUrl, referrer, userAgent);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
    }

}
