package com.springboot.user_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.user_app.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
