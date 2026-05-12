package com.giovanna.saas_billings.listener;

import com.giovanna.saas_billings.dto.BillingDto;
import com.giovanna.saas_billings.dto.BillingEventDto;
import com.giovanna.saas_billings.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BillingEventListener {
    private final BillingService billingService;

    @KafkaListener(topics = "billing-events", groupId = "billing-group")
    public void handleSubscriptionEvent(BillingEventDto billingEventDto) {
        billingService.save(new BillingDto(billingEventDto.subscriptionId(), billingEventDto.amount(), LocalDateTime.now()));
    }
}