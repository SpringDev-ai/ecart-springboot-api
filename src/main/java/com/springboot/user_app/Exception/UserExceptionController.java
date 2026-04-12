package com.springboot.user_app.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springboot.user_app.customException.EmailNotFoundException;
import com.springboot.user_app.customException.IdNotFoundException;
import com.springboot.user_app.customException.IncorrectPasswordException;
import com.springboot.user_app.response.ResponseStructure;

@RestControllerAdvice
public class UserExceptionController {

	@ExceptionHandler(value = IdNotFoundException.class)
	public ResponseEntity<?> idNotFound(IdNotFoundException e){
		ResponseStructure<IdNotFoundException> structure =new ResponseStructure<>();
		structure.setData(e);
		structure.setMessage(e.getMessage());
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(500);
		return new ResponseEntity<>(structure,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(value = EmailNotFoundException.class)
	public ResponseEntity<?> emailNotFound(EmailNotFoundException e){
		ResponseStructure<EmailNotFoundException> structure =new ResponseStructure<>();
		structure.setData(e);
		structure.setMessage(e.getMessage());
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(500);
		return new ResponseEntity<>(structure,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(value = IncorrectPasswordException.class)
	public ResponseEntity<?> incorrectpassword(IncorrectPasswordException e){
		ResponseStructure<IncorrectPasswordException> structure =new ResponseStructure<>();
		structure.setData(e);
		structure.setMessage(e.getMessage());
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(500);
		return new ResponseEntity<>(structure,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
