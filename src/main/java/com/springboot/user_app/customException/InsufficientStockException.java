package com.springboot.user_app.customException;

public class InsufficientStockException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Insufficient Stock";
	}

	public InsufficientStockException() {
	}

	public InsufficientStockException(String message) {
		super(message);
	}
}
