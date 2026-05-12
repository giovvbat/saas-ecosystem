package com.giovanna.saas_billings.repository;

import com.giovanna.saas_billings.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, String> {
    @Modifying
    @Query("UPDATE Billing b SET b.status = 'EXPIRED' WHERE b.registration < b.expiration AND b.status = 'PENDING'")
    void expire();

    @Query("SELECT b.subscriptionId FROM Billing b WHERE b.registration < b.expiration AND b.status = 'PENDING'")
    List<String> retrieveExpiringSubscriptionIds();
}
