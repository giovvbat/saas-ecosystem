package com.giovanna.saas_subscriptions.dto;

public record SubscriptionEventDto(String userId, String subscriptionId, String status, int paymentRequests, String timestamp) {
}
