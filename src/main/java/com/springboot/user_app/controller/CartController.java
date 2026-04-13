package com.springboot.user_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.user_app.dto.AddToCartRequestDto;
import com.springboot.user_app.dto.CartResponseDto;
import com.springboot.user_app.dto.UpdateCartItemRequestDto;
import com.springboot.user_app.response.ResponseStructure;
import com.springboot.user_app.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	CartService cartService;

	@PostMapping("/add")
	public ResponseEntity<?> addToCart(@RequestBody AddToCartRequestDto request) {
		ResponseStructure<CartResponseDto> structure = cartService.addToCart(
				request.getUserId(), request.getProductId(), request.getQuantity());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getCart(@PathVariable Long userId) {
		ResponseStructure<CartResponseDto> structure = cartService.getCartByUserId(userId);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateCartItem(@RequestBody UpdateCartItemRequestDto request) {
		ResponseStructure<CartResponseDto> structure = cartService.updateCartItemQuantity(
				request.getCartItemId(), request.getQuantity());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	@DeleteMapping("/remove/{cartItemId}")
	public ResponseEntity<?> removeCartItem(@PathVariable Long cartItemId) {
		ResponseStructure<String> structure = cartService.removeCartItem(cartItemId);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	@DeleteMapping("/clear/{userId}")
	public ResponseEntity<?> clearCart(@PathVariable Long userId) {
		ResponseStructure<String> structure = cartService.clearCart(userId);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}
