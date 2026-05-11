package com.giovanna.saas_subscriptions.listener;

import com.giovanna.saas_subscriptions.dto.SubscriptionEventDto;
import com.giovanna.saas_subscriptions.enums.SubscriptionStatus;
import com.giovanna.saas_subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionEventListener {
    private final SubscriptionService subscriptionService;

    @KafkaListener(topics = "subscription-events", groupId = "subscription-group")
    public void handleSubscriptionEvent(SubscriptionEventDto subscriptionEventDto) {
        subscriptionService.update(subscriptionEventDto.id(), SubscriptionStatus.from(subscriptionEventDto.status()));
    }
}
