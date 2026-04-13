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
	public ResponseEntity<?> idNotFound(IdNotFoundException e) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(e.getMessage());
		structure.setMessage("ID Not Found");
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(404);
		return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = EmailNotFoundException.class)
	public ResponseEntity<?> emailNotFound(EmailNotFoundException e) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(e.getMessage());
		structure.setMessage("Email Not Found");
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(404);
		return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = IncorrectPasswordException.class)
	public ResponseEntity<?> incorrectpassword(IncorrectPasswordException e) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(e.getMessage());
		structure.setMessage("Incorrect Password");
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(401);
		return new ResponseEntity<>(structure, HttpStatus.UNAUTHORIZED);
	}
}
