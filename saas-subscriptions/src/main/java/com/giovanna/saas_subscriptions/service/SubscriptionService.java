package com.giovanna.saas_subscriptions.service;

import com.giovanna.saas_subscriptions.dto.PayRequestDto;
import com.giovanna.saas_subscriptions.dto.SubscriptionDto;
import com.giovanna.saas_subscriptions.dto.SubscriptionEventDto;
import com.giovanna.saas_subscriptions.dto.UserResponseDto;
import com.giovanna.saas_subscriptions.enums.SubscriptionStatus;
import com.giovanna.saas_subscriptions.exception.InvalidOperationException;
import com.giovanna.saas_subscriptions.exception.ResourceNotFoundException;
import com.giovanna.saas_subscriptions.model.Plan;
import com.giovanna.saas_subscriptions.model.Subscription;
import com.giovanna.saas_subscriptions.publisher.SubscriptionEventPublisher;
import com.giovanna.saas_subscriptions.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository repository;
    private final PlanService planService;
    private final RestClient userRestClient;
    private final SubscriptionEventPublisher subscriptionEventPublisher;

    @Transactional
    public Subscription save(SubscriptionDto subscriptionDto) {
        Plan plan = planService.retrieve(subscriptionDto.planId());

        if (!plan.isActive()) {
            throw new InvalidOperationException("provided plan is not active");
        }

        try {
            userRestClient.get().uri("/api/users/{id}", subscriptionDto.userId());
        } catch (RestClientResponseException exception) {
            throw new ResourceNotFoundException("user", subscriptionDto.userId());
        }

        if (repository.existsByUserIdAndPlanAndStatusIsIn(subscriptionDto.userId(), plan, List.of(SubscriptionStatus.PENDING, SubscriptionStatus.ACTIVE))) {
            throw new InvalidOperationException("user already has equivalent active or pending plan subscription");
        }

        Subscription subscription = new Subscription();
        BeanUtils.copyProperties(subscriptionDto, subscription);

        subscription.setStatus(SubscriptionStatus.PENDING);
        subscription.setPlan(plan);
        subscription.setPaymentRequests(0);

        return repository.save(subscription);
    }

    public Subscription retrieve(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("subscription", id));
    }

    @Transactional
    public Subscription cancel(String id) {
        Subscription subscription = retrieve(id);

        if (subscription.getStatus() == SubscriptionStatus.CANCELED) {
            throw new InvalidOperationException("subscription is already cancelled");
        }

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionEventPublisher.publish(new SubscriptionEventDto(subscription.getUserId(), subscription.getId(), subscription.getStatus().name(), subscription.getPaymentRequests(), LocalDateTime.now().toString()));

        return repository.save(subscription);
    }

    @Transactional
    public Subscription pay(String id, PayRequestDto payRequestDto) {
        Subscription subscription = retrieve(id);

        if (subscription.getStatus() != SubscriptionStatus.PENDING) {
            throw new InvalidOperationException("subscriptions must be pending in order to be paid for");
        }

        subscription.setPaymentRequests(subscription.getPaymentRequests() + 1);

        if (payRequestDto.success()) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        } else {
            if (subscription.getPaymentRequests() >= 3) {
                subscription.setStatus(SubscriptionStatus.FAILED);
            }
        }

        subscriptionEventPublisher.publish(new SubscriptionEventDto(subscription.getUserId(), subscription.getId(), subscription.getStatus().name(), subscription.getPaymentRequests(), LocalDateTime.now().toString()));

        return repository.save(subscription);
    }
}
