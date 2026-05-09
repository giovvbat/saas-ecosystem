package com.giovanna.saas_notifications.dto;

import com.giovanna.saas_notifications.enums.NotificationType;

public record MessageContentDto(String subject, String body, NotificationType type) {
}
