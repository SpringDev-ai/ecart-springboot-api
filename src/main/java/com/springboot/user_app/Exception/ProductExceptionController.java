package com.springboot.user_app.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springboot.user_app.customException.CartNotFoundException;
import com.springboot.user_app.customException.EmptyCartException;
import com.springboot.user_app.customException.IdNotFoundException;
import com.springboot.user_app.customException.InsufficientStockException;
import com.springboot.user_app.customException.OrderNotFoundException;
import com.springboot.user_app.customException.ProductNotFoundException;
import com.springboot.user_app.response.ResponseStructure;

@RestControllerAdvice
public class ProductExceptionController {

	@ExceptionHandler(value = ProductNotFoundException.class)
	public ResponseEntity<?> productNotFound(ProductNotFoundException e) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(e.getMessage());
		structure.setMessage("Product Not Found");
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(404);
		return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = OrderNotFoundException.class)
	public ResponseEntity<?> orderNotFound(OrderNotFoundException e) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(e.getMessage());
		structure.setMessage("Order Not Found");
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(404);
		return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = CartNotFoundException.class)
	public ResponseEntity<?> cartNotFound(CartNotFoundException e) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(e.getMessage());
		structure.setMessage("Cart Not Found");
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(404);
		return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = InsufficientStockException.class)
	public ResponseEntity<?> insufficientStock(InsufficientStockException e) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(e.getMessage());
		structure.setMessage("Insufficient Stock");
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(400);
		return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EmptyCartException.class)
	public ResponseEntity<?> emptyCart(EmptyCartException e) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData(e.getMessage());
		structure.setMessage("Empty Cart");
		structure.setTime(LocalDateTime.now());
		structure.setHttpStatusCode(400);
		return new ResponseEntity<>(structure, HttpStatus.BAD_REQUEST);
	}
}
