package com.learn.tinyurl.service;

import com.learn.tinyurl.dto.ClickedEvent;
import com.learn.tinyurl.entity.Click;
import com.learn.tinyurl.entity.Url;
import com.learn.tinyurl.repository.ClickRepository;
import com.learn.tinyurl.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClickConsumer {

    private final UrlRepository urlRepository;
    private final ClickRepository clickRepository;

    // This class will consume the ClickedEvent messages from Kafka and process them.
    // You can implement the logic to save the click events to the database here.
    @KafkaListener(topics = "click-events", groupId = "click-consumer")
    public void consumeClickedEvent(ClickedEvent message) {
        System.out.println("Received ClickedEvent: " + message);
        Url url = urlRepository.findByShortCode(message.shortCode());
        if (url != null) {
            Click click = new Click();
            click.setUrlId(url.getId());
            click.setReferrer(message.referrer());
            click.setUserAgent(message.userAgent());
            click.setClickedAt(java.time.LocalDateTime.now());
            clickRepository.save(click);
        }
    }
}
