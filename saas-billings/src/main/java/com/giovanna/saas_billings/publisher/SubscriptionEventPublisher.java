package com.giovanna.saas_billings.publisher;

import com.giovanna.saas_billings.dto.SubscriptionEventDto;
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