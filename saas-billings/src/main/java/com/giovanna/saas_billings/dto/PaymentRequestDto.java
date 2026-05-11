package com.giovanna.saas_billings.dto;

import jakarta.validation.constraints.NotNull;

public record PaymentRequestDto(@NotNull boolean success) {
}
