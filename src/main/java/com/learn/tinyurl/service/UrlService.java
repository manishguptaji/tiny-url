package com.learn.tinyurl.service;

import com.learn.tinyurl.entity.Url;
import com.learn.tinyurl.exception.UrlExpiredException;
import com.learn.tinyurl.repository.UrlRepository;
import com.learn.tinyurl.util.ShortCodeGenerator;
import com.learn.tinyurl.exception.AliasNotAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "shorturl:";
    private static final Duration CACHE_TTL = Duration.ofHours(1); // Set the cache TTL to 1 hour

    private static final Set<String> reserved = Set.of(
            "admin", "login", "signup", "api", "user", "dashboard", "settings", "profile", "help", "support", "contact", "about", "terms", "privacy"
    );

    private final UrlRepository urlRepository;

    public Url createSimpleUrl(String longUrl, String customAlias, LocalDateTime expiryTime) {
        if (customAlias != null && !customAlias.isEmpty()) {
            return createAliasUrl(longUrl, customAlias, expiryTime);
        } else {
            return createRandomUrl(longUrl, expiryTime);
        }
    }

    public Url createAliasUrl(String longUrl, String customAlias, LocalDateTime expiryTime) {
        if (reserved.contains(customAlias)) {
            throw new AliasNotAvailableException("The custom alias is reserved and cannot be used.");
        }

        // An alias is the short code itself.  Do this check before saving so a
        // user gets a meaningful response instead of silently receiving a
        // generated code.
        if (urlRepository.findByShortCode(customAlias) != null) {
            throw new AliasNotAvailableException("The custom alias is already in use. Please choose a different one.");
        }

        try {
            // Create a new Url entity
            Url url = new Url();
            url.setShortCode(customAlias);
            url.setLongUrl(longUrl);
            url.setUserId(1L); // Set the user ID to 1 for now todo mg
            url.setExpiresAt(expiryTime);
            return urlRepository.save(url);
        } catch (DataIntegrityViolationException d) {
            throw new AliasNotAvailableException("The custom alias is already in use. Please choose a different one.");
        } catch (Exception e) {
            throw new AliasNotAvailableException("Error occurred while creating alias URL.");
        }
    }

    public Url createRandomUrl(String longUrl, LocalDateTime expiryTime) {
        try {
            // Generate a short code for the custom alias
            String shortCode = ShortCodeGenerator.generateShortCode();
            // Create a new Url entity
            Url url = new Url();
            url.setShortCode(shortCode);
            url.setLongUrl(longUrl);
            url.setUserId(1L); // Set the user ID to 1 for now todo mg
            url.setExpiresAt(expiryTime);
            // Set other fields as needed
            return urlRepository.save(url);
        } catch (DataIntegrityViolationException d) {
            throw new AliasNotAvailableException("Url already in use. Please choose a different one.");
        } catch (Exception e) {
            throw new AliasNotAvailableException("Error occurred while creating alias URL.");
        }
    }

    public String getLongUrl(String shortUrl) {

        // Check if the short URL exists in Redis cache
        String cachedLongUrl = redisTemplate.opsForValue().get(KEY_PREFIX + shortUrl);
        if (cachedLongUrl != null) {
            return cachedLongUrl;
        }

        Url url = urlRepository.findByShortCode(shortUrl);
        LocalDateTime expiryTime = url != null ? url.getExpiresAt() : null;
        if (expiryTime != null && expiryTime.isBefore(LocalDateTime.now())) {
            throw new UrlExpiredException("The URL has expired and is no longer valid.");
        }

        Duration ttl = CACHE_TTL;
        if (expiryTime != null) {
            Duration timeUntilExpiry = Duration.between(LocalDateTime.now(), expiryTime);
            if (timeUntilExpiry.isNegative() || timeUntilExpiry.isZero()) {
                return null;
            }
            ttl = timeUntilExpiry.compareTo(CACHE_TTL) < 0 ? timeUntilExpiry : CACHE_TTL;
        }

        // Store the long URL in Redis cache with a TTL
        if (url != null && ttl.isPositive()) {
            redisTemplate.opsForValue().set(KEY_PREFIX + shortUrl, url.getLongUrl(), ttl);
        }
        return url != null ? url.getLongUrl() : null;
    }
}
