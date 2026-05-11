package com.giovanna.saas_billings.controller;

import com.giovanna.saas_billings.dto.PaymentRequestDto;
import com.giovanna.saas_billings.model.Billing;
import com.giovanna.saas_billings.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billings")
@RequiredArgsConstructor
public class BillingController {
    private final BillingService service;

    @PatchMapping("/pay/{id}")
    public ResponseEntity<Billing> pay(@PathVariable String id, @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(service.pay(id, paymentRequestDto));
    }
}
