package com.giovanna.saas_subscriptions.service;

import com.giovanna.saas_subscriptions.dto.*;
import com.giovanna.saas_subscriptions.enums.SubscriptionStatus;
import com.giovanna.saas_subscriptions.exception.InvalidOperationException;
import com.giovanna.saas_subscriptions.exception.ResourceNotFoundException;
import com.giovanna.saas_subscriptions.model.Plan;
import com.giovanna.saas_subscriptions.model.Subscription;
import com.giovanna.saas_subscriptions.publisher.BillingEventPublisher;
import com.giovanna.saas_subscriptions.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository repository;
    private final PlanService planService;
    private final RestClient userRestClient;
    private final BillingEventPublisher billingEventPublisher;

    @Transactional
    public Subscription save(SubscriptionDto subscriptionDto) {
        Plan plan = planService.retrieve(subscriptionDto.planId());

        if (!plan.isActive()) {
            throw new InvalidOperationException("provided plan is not active");
        }

        try {
            userRestClient.get().uri("/api/users/{id}", subscriptionDto.userId()).retrieve().toBodilessEntity();
        } catch (RestClientResponseException exception) {
            throw new ResourceNotFoundException("unable to make sure user with id " + subscriptionDto.userId() + " exists");
        }

        if (repository.existsByUserIdAndPlanAndStatusIsIn(subscriptionDto.userId(), plan, List.of(SubscriptionStatus.PENDING, SubscriptionStatus.ACTIVE))) {
            throw new InvalidOperationException("user already has equivalent active or pending plan subscription");
        }

        Subscription subscription = new Subscription();
        BeanUtils.copyProperties(subscriptionDto, subscription);

        subscription.setStatus(SubscriptionStatus.PENDING);
        subscription.setPlan(plan);
        subscription.setRegistration(LocalDateTime.now());
        subscription.setExpiration(generateExpiration(subscription));

        subscription = repository.save(subscription);

        billingEventPublisher.publish(new BillingEventDto(subscription.getId(), plan.getPrice()));

        return subscription;
    }

    public Subscription retrieve(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("subscription", id));
    }

    @Transactional
    public Subscription cancel(String id) {
        Subscription subscription = retrieve(id);

        if (!(subscription.getStatus() == SubscriptionStatus.PENDING) && !(subscription.getStatus() == SubscriptionStatus.ACTIVE)) {
            throw new InvalidOperationException("subscription must be pending or active in order to be canceled");
        }

        subscription.setStatus(SubscriptionStatus.CANCELED);

        return repository.save(subscription);
    }

    @Transactional
    public void update(String id, SubscriptionStatus status) {
        Subscription subscription = retrieve(id);
        subscription.setStatus(status);

        repository.save(subscription);
    }

    @Transactional
    public void finish() {
        repository.finish();
    }

    private LocalDateTime generateExpiration(Subscription subscription) {
        return switch (subscription.getPlan().getType()) {
            case MONTHLY -> subscription.getRegistration().plusMonths(1);
            case SEMI_ANNUAL -> subscription.getRegistration().plusMonths(6);
            case ANNUAL -> subscription.getRegistration().plusMonths(12);
        };
    }
}
