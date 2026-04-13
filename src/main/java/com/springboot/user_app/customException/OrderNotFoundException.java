package com.springboot.user_app.customException;

public class OrderNotFoundException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Order Not Found";
	}

	public OrderNotFoundException() {
	}

	public OrderNotFoundException(String message) {
		super(message);
	}
}
