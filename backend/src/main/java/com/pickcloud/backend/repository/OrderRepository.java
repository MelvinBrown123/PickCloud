package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Provides persistence operations for a customer orders.
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByNegocio_IdNegocio(Integer idNegocio);
}
