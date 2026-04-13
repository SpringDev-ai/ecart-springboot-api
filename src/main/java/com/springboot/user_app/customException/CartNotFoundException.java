package com.springboot.user_app.customException;

public class CartNotFoundException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Cart Not Found";
	}

	public CartNotFoundException() {
	}

	public CartNotFoundException(String message) {
		super(message);
	}
}
