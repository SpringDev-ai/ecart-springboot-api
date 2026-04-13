package com.springboot.user_app.customException;

public class EmailNotFoundException extends RuntimeException{
	@Override
	public String getMessage() {
		return "Invalid Email";
	}
	
}
