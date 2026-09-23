package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNegocio_IdNegocio(Integer idNegocio);
}
