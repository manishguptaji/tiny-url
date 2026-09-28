package com.learn.tinyurl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "clicks")
@Getter
@Setter
@NoArgsConstructor
public class Click {
    //id BIGINT NOT NULL AUTO_INCREMENT,
    //url_id BIGINT NOT NULL,
    //clicked_at DATETIME(6) NULL,
    //referrer VARCHAR(2048) NULL,
    //user_agent VARCHAR(512) NULL,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url_id", nullable = false)
    private Long urlId;
    @Column(name = "clicked_at")
    private LocalDateTime clickedAt;
    @Column(name = "referrer")
    private String referrer;
    @Column(name = "user_agent")
    private String userAgent;
}
