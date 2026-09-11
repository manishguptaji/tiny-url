package com.learn.tinyurl.service;

import com.learn.tinyurl.entity.Url;
import com.learn.tinyurl.repository.UrlRepository;
import com.learn.tinyurl.util.ShortCodeGenerator;
import com.learn.tinyurl.exception.AliasNotAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UrlService {

    private static final Set<String> reserved = Set.of(
            "admin", "login", "signup", "api", "user", "dashboard", "settings", "profile", "help", "support", "contact", "about", "terms", "privacy"
    );

    private final UrlRepository urlRepository;

    public Url createSimpleUrl(String longUrl, String customAlias) {
        if (customAlias != null && !customAlias.isEmpty()) {
            return createAliasUrl(longUrl, customAlias);
        } else {
            return createRandomUrl(longUrl);
        }
    }

    public Url createAliasUrl(String longUrl, String customAlias) {
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
            return urlRepository.save(url);
        } catch (DataIntegrityViolationException d) {
            throw new AliasNotAvailableException("The custom alias is already in use. Please choose a different one.");
        } catch (Exception e) {
            throw new AliasNotAvailableException("Error occurred while creating alias URL.");
        }
    }

    public Url createRandomUrl(String longUrl) {
        try {
            // Generate a short code for the custom alias
            String shortCode = ShortCodeGenerator.generateShortCode();
            // Create a new Url entity
            Url url = new Url();
            url.setShortCode(shortCode);
            url.setLongUrl(longUrl);
            url.setUserId(1L); // Set the user ID to 1 for now todo mg
            // Set other fields as needed
            return urlRepository.save(url);
        } catch (DataIntegrityViolationException d) {
            throw new AliasNotAvailableException("Url already in use. Please choose a different one.");
        } catch (Exception e) {
            throw new AliasNotAvailableException("Error occurred while creating alias URL.");
        }
    }
}
