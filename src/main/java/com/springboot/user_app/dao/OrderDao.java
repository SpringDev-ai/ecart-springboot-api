package com.springboot.user_app.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.user_app.entity.Order;
import com.springboot.user_app.entity.OrderItem;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.repository.OrderItemRepository;
import com.springboot.user_app.repository.OrderRepository;

@Repository
public class OrderDao {

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	OrderItemRepository orderItemRepo;

	public Order saveOrder(Order order) {
		return orderRepo.save(order);
	}

	public Optional<Order> getOrderById(Long orderId) {
		return orderRepo.findById(orderId);
	}

	public List<Order> getOrdersByUser(User user) {
		return orderRepo.findByUser(user);
	}

	public List<Order> getOrdersByStatus(String status) {
		return orderRepo.findByOrderStatus(status);
	}

	public OrderItem saveOrderItem(OrderItem orderItem) {
		return orderItemRepo.save(orderItem);
	}

	public void deleteOrder(Long orderId) {
		orderRepo.deleteById(orderId);
	}
}
