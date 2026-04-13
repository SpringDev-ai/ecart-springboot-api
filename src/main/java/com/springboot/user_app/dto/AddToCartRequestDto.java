package com.springboot.user_app.dto;

import lombok.Data;

@Data
public class AddToCartRequestDto {
	private Long userId;
	private Long productId;
	private int quantity;
}
