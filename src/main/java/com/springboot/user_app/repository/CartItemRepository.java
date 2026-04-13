package com.springboot.user_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.user_app.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
