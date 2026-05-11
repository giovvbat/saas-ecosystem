package com.giovanna.saas_billings.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class SubscriptionRestClientConfig {
    @Value("${subscription.service.url}")
    private String baseUrl;

    @Bean
    public RestClient subscriptionRestClient() {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
