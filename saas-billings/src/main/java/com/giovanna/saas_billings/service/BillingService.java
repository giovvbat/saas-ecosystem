package com.giovanna.saas_billings.service;

import com.giovanna.saas_billings.dto.BillingDto;
import com.giovanna.saas_billings.dto.PaymentRequestDto;
import com.giovanna.saas_billings.dto.SubscriptionEventDto;
import com.giovanna.saas_billings.enums.BillingStatus;
import com.giovanna.saas_billings.enums.SubscriptionStatus;
import com.giovanna.saas_billings.exception.InvalidOperationException;
import com.giovanna.saas_billings.exception.ResourceNotFoundException;
import com.giovanna.saas_billings.model.Billing;
import com.giovanna.saas_billings.publisher.SubscriptionEventPublisher;
import com.giovanna.saas_billings.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {
    private final BillingRepository repository;
    private final SubscriptionEventPublisher subscriptionEventPublisher;

    @Transactional
    public void save(BillingDto billingDto) {
        Billing billing = new Billing();
        BeanUtils.copyProperties(billingDto, billing);

        billing.setPaymentRequests(0);
        billing.setStatus(BillingStatus.PENDING);
        billing.setRegistration(LocalDateTime.now());
        billing.setExpiration(billing.getRegistration().plusDays(7));

        repository.save(billing);
    }

    public Billing retrieve(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("billing", id));
    }

    public Billing pay(String id, PaymentRequestDto paymentRequestDto) {
        Billing billing = retrieve(id);

        if (billing.getStatus() != BillingStatus.PENDING) {
            throw new InvalidOperationException("billings must be pending in order to be paid for");
        }

        billing.setPaymentRequests(billing.getPaymentRequests() + 1);

        if (paymentRequestDto.success()) {
            billing.setStatus(BillingStatus.PAID);
            subscriptionEventPublisher.publish(new SubscriptionEventDto(billing.getSubscriptionId(), SubscriptionStatus.ACTIVE.name()));
        } else {
            if (billing.getPaymentRequests() >= 3) {
                billing.setStatus(BillingStatus.FAILED);
                subscriptionEventPublisher.publish(new SubscriptionEventDto(billing.getSubscriptionId(), SubscriptionStatus.FAILED.name()));
            }
        }

        return repository.save(billing);
    }

    @Transactional
    public void expire() {
        List<String> expired = repository.retrieveExpiringSubscriptionIds();

        if (!expired.isEmpty()) {
            repository.expire();
            expired.forEach(id -> subscriptionEventPublisher.publish(new SubscriptionEventDto(id, SubscriptionStatus.EXPIRED.name())));
        }
    }
}
