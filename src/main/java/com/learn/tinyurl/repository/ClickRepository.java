package com.learn.tinyurl.repository;

import com.learn.tinyurl.entity.Click;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickRepository extends JpaRepository<Click, Long> {
    Click findByUrlId(Long urlId);
}
