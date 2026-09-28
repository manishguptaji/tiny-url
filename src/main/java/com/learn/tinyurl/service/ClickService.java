package com.learn.tinyurl.service;

import com.learn.tinyurl.dto.ClickedEvent;
import com.learn.tinyurl.entity.Click;
import com.learn.tinyurl.entity.Url;
import com.learn.tinyurl.exception.UrlDoesNotExistException;
import com.learn.tinyurl.repository.ClickRepository;
import com.learn.tinyurl.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClickService {

    private final ClickRepository clickRepository;
    private final UrlRepository urlRepository;

    private final String KAFKA_TOPIC = "click-events";
    private final KafkaTemplate<String, ClickedEvent> kafkaTemplate;

    public void recordClick(String shortCode, String referrer, String userAgent) {
        // Create a ClickedEvent object
        ClickedEvent clickedEvent = new ClickedEvent(shortCode,
                java.time.LocalDateTime.now().toString(),
                referrer,
                userAgent);

        // Send the event to Kafka
        kafkaTemplate.send(KAFKA_TOPIC, clickedEvent);
    }

}
