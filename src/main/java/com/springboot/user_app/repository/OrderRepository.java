package com.springboot.user_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.user_app.entity.Order;
import com.springboot.user_app.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser(User user);
	List<Order> findByOrderStatus(String orderStatus);
}
