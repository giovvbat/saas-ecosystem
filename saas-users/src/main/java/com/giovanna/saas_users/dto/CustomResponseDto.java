package com.giovanna.saas_users.dto;

import java.time.LocalDateTime;

public record CustomResponseDto(int code, String status, String message, LocalDateTime timestamp) {
}
