package com.giovanna.saas_billings.dto;

import java.time.LocalDateTime;

public record BillingDto(String subscriptionId, double amount) {
}
