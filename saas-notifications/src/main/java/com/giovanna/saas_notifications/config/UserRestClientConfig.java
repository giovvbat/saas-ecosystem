package com.giovanna.saas_notifications.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class UserRestClientConfig {
    @Value("${user.service.url}")
    private String baseUrl;

    @Bean
    public RestClient userRestClient() {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
