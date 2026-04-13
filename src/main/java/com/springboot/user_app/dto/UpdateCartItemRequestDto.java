package com.springboot.user_app.dto;

import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
	private Long cartItemId;
	private int quantity;
}
