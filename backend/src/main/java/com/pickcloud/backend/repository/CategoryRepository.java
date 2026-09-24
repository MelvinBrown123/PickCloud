package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/** Provides standard JPA persistence operations for Category entities. */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
