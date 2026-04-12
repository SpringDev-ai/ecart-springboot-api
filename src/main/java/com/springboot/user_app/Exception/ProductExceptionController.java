package com.springboot.user_app.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springboot.user_app.customException.IdNotFoundException;
import com.springboot.user_app.customException.ProductNotFoundException;
import com.springboot.user_app.response.ResponseStructure;

@RestControllerAdvice
public class ProductExceptionController {
	@ExceptionHandler(value = ProductNotFoundException.class)
	public ResponseEntity<?> productNotFound(ProductNotFoundException e){
		ResponseStructure<ProductNotFoundException> structure =new ResponseStructure<>();
		structure.setData(e);
		structure.setMessage(e.getMessage());
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(500);
		return new ResponseEntity<>(structure,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
