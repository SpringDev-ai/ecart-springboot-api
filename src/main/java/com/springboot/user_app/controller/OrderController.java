package com.springboot.user_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.user_app.dto.OrderResponseDto;
import com.springboot.user_app.response.ResponseStructure;
import com.springboot.user_app.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrderService orderService;

	@PostMapping("/place/{userId}")
	public ResponseEntity<?> placeOrder(@PathVariable Long userId) {
		ResponseStructure<OrderResponseDto> structure = orderService.placeOrder(userId);
		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getOrdersByUser(@PathVariable Long userId) {
		ResponseStructure<List<OrderResponseDto>> structure = orderService.getOrdersByUser(userId);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
		ResponseStructure<OrderResponseDto> structure = orderService.getOrderById(orderId);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	@PutMapping("/cancel/{orderId}")
	public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
		ResponseStructure<OrderResponseDto> structure = orderService.cancelOrder(orderId);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}
