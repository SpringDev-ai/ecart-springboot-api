package com.springboot.user_app.customException;


public class IdNotFoundException extends RuntimeException{

	@Override
	public String getMessage() {
		return "IdNotFoundException";
	}

	public IdNotFoundException() {
		
	}

	public IdNotFoundException(String message) {
		super(message);
		
	}
	
	
	
}
