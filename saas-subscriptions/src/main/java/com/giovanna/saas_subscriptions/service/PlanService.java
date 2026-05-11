package com.giovanna.saas_subscriptions.service;

import com.giovanna.saas_subscriptions.dto.PlanDto;
import com.giovanna.saas_subscriptions.exception.InvalidOperationException;
import com.giovanna.saas_subscriptions.exception.ResourceNotFoundException;
import com.giovanna.saas_subscriptions.model.Plan;
import com.giovanna.saas_subscriptions.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository repository;

    @Transactional
    public Plan save(PlanDto planDto) {
        if (repository.existsByName(planDto.name())) {
            throw new InvalidOperationException("plan with name " + planDto.name() + " already exists");
        }

        Plan plan = new Plan();
        BeanUtils.copyProperties(planDto, plan);

        return repository.save(plan);
    }

    public Plan retrieve(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("plan", id));
    }

    @Transactional
    public Plan activate(String id) {
        Plan plan = retrieve(id);

        if (plan.isActive()) {
            throw new InvalidOperationException("plan is already activated");
        }

        plan.setActive(true);

        return repository.save(plan);
    }

    @Transactional
    public Plan deactivate(String id) {
        Plan plan = retrieve(id);

        if (!plan.isActive()) {
            throw new InvalidOperationException("plan is already deactivated");
        }

        plan.setActive(false);

        return repository.save(plan);
    }
}
