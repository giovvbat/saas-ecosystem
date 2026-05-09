package com.giovanna.saas_notifications.factory;

import com.giovanna.saas_notifications.dto.MessageContentDto;
import com.giovanna.saas_notifications.enums.NotificationType;
import com.giovanna.saas_notifications.enums.SubscriptionStatus;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

@Component
public class MessageContentFactory {
    public static MessageContentDto generate(String name, SubscriptionStatus status, int paymentRequests, String timestamp) {
        Pair<NotificationType, Pair<String, String>> content = switch (status) {
            case PENDING -> Pair.of(NotificationType.PAYMENT_FAILED, Pair.of("subscription payment failure :o", String.format("hello, %s! we were unable to process your payment requested at %s. please note that you have %d remaining attempt(s); if these are exceeded, your purchase will be automatically canceled for security reasons. thank you for your understanding.", name, timestamp, (3 - paymentRequests))));
            case FAILED -> Pair.of(NotificationType.SUBSCRIPTION_FAILED, Pair.of("subscription request no longer available :/", String.format("hello, %s! your payment requested at %s was unsuccessful, and, for security reasons, your purchase has been canceled. please feel free to try again. thank you.", name, timestamp)));
            case ACTIVE -> Pair.of(NotificationType.PAYMENT_SUCCESS, Pair.of("subscription payment successful :D", String.format("hello, %s! we have received your payment requested at %s, and everything is now confirmed. please enjoy your subscription and thank you for choosing us.", name, timestamp)));
            case CANCELED -> Pair.of(NotificationType.SUBSCRIPTION_CANCELED, Pair.of("your subscription is canceled :c", String.format("hello, %s! we’re sorry to see you go... your subscription has been successfully canceled at %s. we hope to welcome you back again soon. best regards.", name, timestamp)));
        };

        return new MessageContentDto(content.getSecond().getFirst(), content.getSecond().getSecond(), content.getFirst());
    }
}