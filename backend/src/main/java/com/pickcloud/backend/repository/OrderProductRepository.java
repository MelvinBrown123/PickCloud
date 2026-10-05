package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Provides persistence operations for product associated with orders.
 */
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {

    List<OrderProduct> findByPedido_IdPedido(Integer idPedido);

}
