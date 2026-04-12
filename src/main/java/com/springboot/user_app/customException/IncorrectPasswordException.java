package com.springboot.user_app.customException;

public class IncorrectPasswordException extends RuntimeException{

	@Override
	public String getMessage() {
		return "Incorrect Password";
	}
	

}
