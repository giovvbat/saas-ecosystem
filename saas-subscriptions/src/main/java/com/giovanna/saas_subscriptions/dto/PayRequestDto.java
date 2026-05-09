package com.giovanna.saas_subscriptions.dto;

import jakarta.validation.constraints.NotNull;

public record PayRequestDto(@NotNull boolean success) {
}
