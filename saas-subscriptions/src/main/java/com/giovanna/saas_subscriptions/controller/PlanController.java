package com.giovanna.saas_subscriptions.controller;

import com.giovanna.saas_subscriptions.dto.PlanDto;
import com.giovanna.saas_subscriptions.model.Plan;
import com.giovanna.saas_subscriptions.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService service;

    @PostMapping
    public ResponseEntity<Plan> save(@RequestBody @Valid PlanDto planDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(planDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> retrieve(@PathVariable String id) {
        return ResponseEntity.ok(service.retrieve(id));
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Plan> activate(@PathVariable String id) {
        return ResponseEntity.ok(service.activate(id));
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Plan> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(service.deactivate(id));
    }
}
