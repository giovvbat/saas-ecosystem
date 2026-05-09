package com.giovanna.saas_subscriptions.controller;

import com.giovanna.saas_subscriptions.dto.PayRequestDto;
import com.giovanna.saas_subscriptions.dto.SubscriptionDto;
import com.giovanna.saas_subscriptions.model.Subscription;
import com.giovanna.saas_subscriptions.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService service;

    @PostMapping
    public ResponseEntity<Subscription> save(@RequestBody @Valid SubscriptionDto subscriptionDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(subscriptionDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> retrieve(@PathVariable String id) {
        return ResponseEntity.ok(service.retrieve(id));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<Subscription> cancel(@PathVariable String id) {
        return ResponseEntity.ok(service.cancel(id));
    }

    @PatchMapping("/pay/{id}")
    public ResponseEntity<Subscription> pay(@PathVariable String id, @RequestBody @Valid PayRequestDto payRequestDto) {
        return ResponseEntity.ok(service.pay(id, payRequestDto));
    }
}
