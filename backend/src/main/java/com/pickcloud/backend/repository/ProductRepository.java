package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/** Provides persistence operations and business-scoped product queries. */
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Spring Data JPA derives the query from the method name:
    // Product.negocio -> Business.idNegocio.
    List<Product> findByNegocio_IdNegocio(Integer idNegocio);

    /**
     * Checks whether a SKU is already registered within a specific business.
     * The same SKU may exist in different businesses.
     */
    boolean existsByNegocio_IdNegocioAndSku(Integer idNegocio, String sku);
}
