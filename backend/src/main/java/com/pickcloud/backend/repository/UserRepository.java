package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,Integer> {

}