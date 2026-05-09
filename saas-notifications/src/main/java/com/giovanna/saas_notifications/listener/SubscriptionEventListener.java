package com.giovanna.saas_notifications.listener;

import com.giovanna.saas_notifications.dto.NotificationDto;
import com.giovanna.saas_notifications.dto.SubscriptionEventDto;
import com.giovanna.saas_notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionEventListener {
    private final NotificationService notificationService;

    @KafkaListener(topics = "subscription-events", groupId = "notification-group")
    public void handleSubscriptionEvent(SubscriptionEventDto subscriptionEventDto) {
        notificationService.process(subscriptionEventDto);
    }
}