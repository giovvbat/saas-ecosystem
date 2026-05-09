package com.giovanna.saas_subscriptions.repository;

import com.giovanna.saas_subscriptions.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {
}
