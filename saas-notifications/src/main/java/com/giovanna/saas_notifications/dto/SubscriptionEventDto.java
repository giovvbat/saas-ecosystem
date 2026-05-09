package com.giovanna.saas_notifications.dto;

public record SubscriptionEventDto(String userId, String subscriptionId, String status, int paymentRequests, String timestamp) {
}
