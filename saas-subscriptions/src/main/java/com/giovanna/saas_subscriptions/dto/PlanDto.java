package com.giovanna.saas_subscriptions.dto;

import com.giovanna.saas_subscriptions.enums.PlanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlanDto(@NotNull PlanType type, @Positive Double price, @NotBlank String name, @NotNull boolean active) {
}
