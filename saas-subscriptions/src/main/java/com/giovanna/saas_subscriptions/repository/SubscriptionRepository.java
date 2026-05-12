package com.giovanna.saas_subscriptions.repository;

import com.giovanna.saas_subscriptions.enums.SubscriptionStatus;
import com.giovanna.saas_subscriptions.model.Plan;
import com.giovanna.saas_subscriptions.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    @Modifying
    @Query("UPDATE Subscription s SET s.status = 'FINISHED' WHERE s.registration < s.expiration AND s.status = 'ACTIVE'")
    void finish();

    boolean existsByUserIdAndPlanAndStatusIsIn(String userId, Plan plan, List<SubscriptionStatus> status);
}
