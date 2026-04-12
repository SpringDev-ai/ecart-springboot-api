package com.springboot.user_app.dto;

import lombok.Data;

@Data
public class UserResponseDto {
	private long userId;
	private String name;
	private String email;
}
