package com.learn.tinyurl.controller;

import com.learn.tinyurl.dto.CreateUrlRequest;
import com.learn.tinyurl.dto.CreateUrlResponse;
import com.learn.tinyurl.entity.Url;
import com.learn.tinyurl.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @Value("${app.base-url}")
    private String baseUrl;

    @PostMapping
    public ResponseEntity<CreateUrlResponse> createShortUrl(@Valid @RequestBody CreateUrlRequest request) {
        Url url = urlService.createSimpleUrl(request.longUrl(), request.customAlias(), request.expiresAt());
        CreateUrlResponse response = new CreateUrlResponse(baseUrl + url.getShortCode(),
                url.getLongUrl(),
                request.customAlias(),
                url.getExpiresAt());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Url>> getUrlsByUserId() {
        List<Url> urls = urlService.getUrlsByUserId();
        return ResponseEntity.ok(urls);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUrlById(@PathVariable Long id) {
        urlService.deleteUrlById(id);
        return ResponseEntity.noContent().build();
    }
}
