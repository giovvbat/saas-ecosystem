package com.giovanna.saas_notifications.service;

import com.giovanna.saas_notifications.dto.NotificationDto;
import com.giovanna.saas_notifications.dto.MessageContentDto;
import com.giovanna.saas_notifications.dto.SubscriptionEventDto;
import com.giovanna.saas_notifications.dto.UserResponseDto;
import com.giovanna.saas_notifications.enums.NotificationStatus;
import com.giovanna.saas_notifications.enums.SubscriptionStatus;
import com.giovanna.saas_notifications.exception.ResourceNotFoundException;
import com.giovanna.saas_notifications.factory.MessageContentFactory;
import com.giovanna.saas_notifications.model.Notification;
import com.giovanna.saas_notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    private final RestClient userRestClient;
    private final EmailService emailService;

    private void save(NotificationDto notificationDto) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationDto, notification);

        repository.save(notification);
    }

    @Transactional
    public void process(SubscriptionEventDto subscriptionEventDto) {
        try {
            UserResponseDto userResponseDto = userRestClient.get().uri("/api/users/{id}", subscriptionEventDto.userId()).retrieve().body(UserResponseDto.class);
            MessageContentDto messageContentDto = MessageContentFactory.generate(userResponseDto.name(), SubscriptionStatus.valueOf(subscriptionEventDto.status()), subscriptionEventDto.paymentRequests(), subscriptionEventDto.timestamp());
            NotificationStatus status = emailService.send(userResponseDto.email(), messageContentDto.subject(), messageContentDto.body()) ? NotificationStatus.SENT : NotificationStatus.FAILED;

            save(new NotificationDto(subscriptionEventDto.subscriptionId(), messageContentDto.type(), messageContentDto.subject(), messageContentDto.body(), userResponseDto.email(), status));
        } catch (RestClientResponseException exception) {
            throw new ResourceNotFoundException("user", subscriptionEventDto.userId());
        }
    }
}
