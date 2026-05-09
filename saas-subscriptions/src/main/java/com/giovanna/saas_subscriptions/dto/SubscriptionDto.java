package com.giovanna.saas_subscriptions.dto;

import jakarta.validation.constraints.NotBlank;

public record SubscriptionDto(@NotBlank String planId, @NotBlank String userId) {
}
