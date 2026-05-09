package com.giovanna.saas_subscriptions.model;

import com.giovanna.saas_subscriptions.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "tb_subscriptions")
@Data
public class Subscription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Plan plan;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    private String userId;
    private int paymentRequests;
}