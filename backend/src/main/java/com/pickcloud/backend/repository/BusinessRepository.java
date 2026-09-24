package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

/** Provides standard JPA persistence operations for Business entities. */
public interface BusinessRepository extends JpaRepository<Business, Integer> {
}
