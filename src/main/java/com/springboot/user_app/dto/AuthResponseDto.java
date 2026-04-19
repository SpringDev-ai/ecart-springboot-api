package com.springboot.user_app.dto;

import com.springboot.user_app.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
	private String email;
	private Role role;
	private String authType;
}
