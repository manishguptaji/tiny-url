package com.learn.tinyurl.repository;

import com.learn.tinyurl.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Url findByShortCode(String shortCode);
}
