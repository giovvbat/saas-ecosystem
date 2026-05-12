package com.giovanna.saas_billings.model;

import com.giovanna.saas_billings.enums.BillingStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_billings")
@Data
public class Billing implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private BillingStatus status;

    private String subscriptionId;
    private double amount;
    private int paymentRequests;
    private LocalDateTime registration;
    private LocalDateTime expiration;
}
