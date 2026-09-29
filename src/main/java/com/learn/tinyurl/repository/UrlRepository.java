package com.learn.tinyurl.repository;

import com.learn.tinyurl.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Url findByShortCode(String shortCode);

    List<Url> findByUserId(Long userId);
}
