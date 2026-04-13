package com.springboot.user_app.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderResponseDto {
	private Long orderId;
	private LocalDateTime orderDate;
	private double totalAmount;
	private String orderStatus;
	private String paymentStatus;
	private Long userId;
	private String userName;
	private List<OrderItemDto> items;

	@Data
	public static class OrderItemDto {
		private Long orderItemId;
		private Long productId;
		private String productName;
		private double price;
		private int quantity;
		private double subtotal;
	}
}
