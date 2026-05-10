package com.giovanna.saas_notifications.dto;

import com.giovanna.saas_notifications.enums.NotificationStatus;
import com.giovanna.saas_notifications.enums.NotificationType;

public record NotificationDto(String subscriptionId, String userId, NotificationType type, String subject, String body, String destination, NotificationStatus status) {
}
