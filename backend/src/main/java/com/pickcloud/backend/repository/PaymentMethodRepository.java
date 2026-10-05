package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Provides persistence operations for payment methods.
 */
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
}
