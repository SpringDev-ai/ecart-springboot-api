package com.springboot.user_app.customException;

public class ProductNotFoundException extends RuntimeException {
	
	@Override
	public String getMessage() {
		return "Product Not Found";
	}
	public ProductNotFoundException(){
		
	}
	public ProductNotFoundException(String message) {
		super(message);
		
	}
	

}
