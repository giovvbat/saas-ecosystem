package com.giovanna.saas_subscriptions.publisher;

import com.giovanna.saas_subscriptions.dto.BillingEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillingEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(BillingEventDto billingEventDto) {
        kafkaTemplate.send("billing-events", billingEventDto);
    }
}