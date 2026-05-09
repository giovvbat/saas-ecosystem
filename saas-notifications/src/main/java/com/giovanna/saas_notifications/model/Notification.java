package com.giovanna.saas_notifications.model;

import com.giovanna.saas_notifications.enums.NotificationStatus;
import com.giovanna.saas_notifications.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "tb_notifications")
@Data
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Lob
    private String body;

    private String subscriptionId;
    private String destination;
    private String subject;
}
