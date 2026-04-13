package com.springboot.user_app.customException;

public class EmptyCartException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Cart is empty. Add items before placing an order.";
	}

	public EmptyCartException() {
	}

	public EmptyCartException(String message) {
		super(message);
	}
}
