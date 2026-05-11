package com.giovanna.saas_subscriptions.enums;

public enum SubscriptionStatus {
    ACTIVE,
    PENDING,
    FAILED,
    CANCELED;

    public static SubscriptionStatus from(String status) {
        return switch (status) {
            case "ACTIVE" -> ACTIVE;
            case "PENDING" -> PENDING;
            case "FAILED" -> FAILED;
            case "CANCELED" -> CANCELED;
            default -> throw new IllegalStateException("unexpected value provided as status (\"" + status + "\")");
        };
    }
}
