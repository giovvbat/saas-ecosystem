package com.giovanna.saas_subscriptions.publisher;

import com.giovanna.saas_subscriptions.dto.SubscriptionEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(SubscriptionEventDto subscriptionEventDto) {
        kafkaTemplate.send("subscription-events", subscriptionEventDto);
    }
}