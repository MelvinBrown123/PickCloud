package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/** Provides standard JPA persistence operations for User entities. */
public interface UserRepository extends JpaRepository<User, Integer> {
}
