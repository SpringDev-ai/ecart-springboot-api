package com.springboot.user_app.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {
	private String email;
	private String password;
}
