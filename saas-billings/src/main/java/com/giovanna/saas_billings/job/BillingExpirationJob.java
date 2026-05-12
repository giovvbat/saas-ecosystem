package com.giovanna.saas_billings.job;

import com.giovanna.saas_billings.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillingExpirationJob {
    private final BillingService service;

    @Scheduled(cron = "0 0 0 * * *")
    public void expire() {
        service.expire();
    }
}
