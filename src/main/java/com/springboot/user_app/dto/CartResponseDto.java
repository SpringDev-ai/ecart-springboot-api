package com.springboot.user_app.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartResponseDto {
	private Long cartId;
	private Long userId;
	private String userName;
	private List<CartItemDto> items;
	private double totalPrice;
	private int totalItems;

	@Data
	public static class CartItemDto {
		private Long cartItemId;
		private Long productId;
		private String productName;
		private double productPrice;
		private int quantity;
		private double subtotal;
	}
}
