package com.giovanna.saas_subscriptions.job;
import com.giovanna.saas_subscriptions.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionFinishingJob {
    private final SubscriptionService service;

    @Scheduled(cron = "0 0 0 * * *")
    public void expire() {
        service.finish();
    }
}
