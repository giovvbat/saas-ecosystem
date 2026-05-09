package com.giovanna.saas_subscriptions.repository;

import com.giovanna.saas_subscriptions.enums.SubscriptionStatus;
import com.giovanna.saas_subscriptions.model.Plan;
import com.giovanna.saas_subscriptions.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    boolean existsByUserIdAndPlanAndStatusIsIn(String userId, Plan plan, List<SubscriptionStatus> status);
}
