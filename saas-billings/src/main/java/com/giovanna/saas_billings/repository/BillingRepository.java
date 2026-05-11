package com.giovanna.saas_billings.repository;

import com.giovanna.saas_billings.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, String> {
}
