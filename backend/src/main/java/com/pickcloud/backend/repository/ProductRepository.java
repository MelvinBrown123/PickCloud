package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Provides persistence operations and business-scoped product queries. */
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Spring Data JPA derives the query from the method name:
    // Product.negocio -> Business.idNegocio.
    List<Product> findByNegocio_IdNegocio(Integer idNegocio);
}
