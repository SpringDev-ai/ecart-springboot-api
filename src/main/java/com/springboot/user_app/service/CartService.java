package com.springboot.user_app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.user_app.customException.CartNotFoundException;
import com.springboot.user_app.customException.IdNotFoundException;
import com.springboot.user_app.customException.InsufficientStockException;
import com.springboot.user_app.customException.ProductNotFoundException;
import com.springboot.user_app.dao.CartDao;
import com.springboot.user_app.dao.ProductDao;
import com.springboot.user_app.dao.UserDao;
import com.springboot.user_app.dto.CartResponseDto;
import com.springboot.user_app.entity.Cart;
import com.springboot.user_app.entity.CartItem;
import com.springboot.user_app.entity.Product;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.response.ResponseStructure;

@Service
public class CartService {

	@Autowired
	CartDao cartDao;

	@Autowired
	UserDao userDao;

	@Autowired
	ProductDao productDao;

	// Add item to cart
	public ResponseStructure<CartResponseDto> addToCart(Long userId, Long productId, int quantity) {
		// Validate user
		Optional<User> userOpt = userDao.getUserById(userId);
		if (!userOpt.isPresent()) {
			throw new IdNotFoundException("User not found with id: " + userId);
		}
		User user = userOpt.get();

		// Validate product
		Optional<Product> productOpt = productDao.getProductById(productId);
		if (!productOpt.isPresent()) {
			throw new ProductNotFoundException("Product not found with id: " + productId);
		}
		Product product = productOpt.get();

		// Check stock
		if (product.getStock() < quantity) {
			throw new InsufficientStockException("Not enough stock. Available: " + product.getStock());
		}

		// Get or create cart
		Cart cart = cartDao.getCartByUser(user).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			newCart.setCartItems(new ArrayList<>());
			return cartDao.saveCart(newCart);
		});

		// Check if product already in cart
		Optional<CartItem> existingItem = cart.getCartItems().stream()
				.filter(item -> item.getProduct().getProductId().equals(productId))
				.findFirst();

		if (existingItem.isPresent()) {
			CartItem item = existingItem.get();
			int newQuantity = item.getQuantity() + quantity;
			if (product.getStock() < newQuantity) {
				throw new InsufficientStockException("Not enough stock. Available: " + product.getStock());
			}
			item.setQuantity(newQuantity);
			cartDao.saveCartItem(item);
		} else {
			CartItem newItem = new CartItem();
			newItem.setCart(cart);
			newItem.setProduct(product);
			newItem.setQuantity(quantity);
			cart.getCartItems().add(newItem);
			cartDao.saveCart(cart);
		}

		// Build response
		CartResponseDto responseDto = buildCartResponseDto(cart);
		ResponseStructure<CartResponseDto> structure = new ResponseStructure<>();
		structure.setData(responseDto);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Product added to cart successfully");
		structure.setHttpStatusCode(200);
		return structure;
	}

	// Get cart by user ID
	public ResponseStructure<CartResponseDto> getCartByUserId(Long userId) {
		Optional<User> userOpt = userDao.getUserById(userId);
		if (!userOpt.isPresent()) {
			throw new IdNotFoundException("User not found with id: " + userId);
		}
		User user = userOpt.get();

		Cart cart = cartDao.getCartByUser(user)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

		CartResponseDto responseDto = buildCartResponseDto(cart);
		ResponseStructure<CartResponseDto> structure = new ResponseStructure<>();
		structure.setData(responseDto);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Cart retrieved successfully");
		structure.setHttpStatusCode(200);
		return structure;
	}

	// Update cart item quantity
	public ResponseStructure<CartResponseDto> updateCartItemQuantity(Long cartItemId, int quantity) {
		Optional<CartItem> cartItemOpt = cartDao.getCartItemById(cartItemId);
		if (!cartItemOpt.isPresent()) {
			throw new CartNotFoundException("Cart item not found with id: " + cartItemId);
		}
		CartItem cartItem = cartItemOpt.get();

		// Check stock
		if (cartItem.getProduct().getStock() < quantity) {
			throw new InsufficientStockException(
					"Not enough stock. Available: " + cartItem.getProduct().getStock());
		}

		if (quantity <= 0) {
			// Remove item if quantity is 0 or less
			Cart cart = cartItem.getCart();
			cart.getCartItems().remove(cartItem);
			cartDao.saveCart(cart);

			CartResponseDto responseDto = buildCartResponseDto(cart);
			ResponseStructure<CartResponseDto> structure = new ResponseStructure<>();
			structure.setData(responseDto);
			structure.setTime(LocalDateTime.now());
			structure.setMessage("Cart item removed");
			structure.setHttpStatusCode(200);
			return structure;
		}

		cartItem.setQuantity(quantity);
		cartDao.saveCartItem(cartItem);

		Cart cart = cartItem.getCart();
		CartResponseDto responseDto = buildCartResponseDto(cart);
		ResponseStructure<CartResponseDto> structure = new ResponseStructure<>();
		structure.setData(responseDto);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Cart item updated successfully");
		structure.setHttpStatusCode(200);
		return structure;
	}

	// Remove item from cart
	public ResponseStructure<String> removeCartItem(Long cartItemId) {
		Optional<CartItem> cartItemOpt = cartDao.getCartItemById(cartItemId);
		if (!cartItemOpt.isPresent()) {
			throw new CartNotFoundException("Cart item not found with id: " + cartItemId);
		}
		CartItem cartItem = cartItemOpt.get();
		Cart cart = cartItem.getCart();
		cart.getCartItems().remove(cartItem);
		cartDao.saveCart(cart);

		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData("Item removed from cart");
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Cart item removed successfully");
		structure.setHttpStatusCode(200);
		return structure;
	}

	// Clear entire cart
	public ResponseStructure<String> clearCart(Long userId) {
		Optional<User> userOpt = userDao.getUserById(userId);
		if (!userOpt.isPresent()) {
			throw new IdNotFoundException("User not found with id: " + userId);
		}
		User user = userOpt.get();

		Optional<Cart> cartOpt = cartDao.getCartByUser(user);
		if (cartOpt.isPresent()) {
			Cart cart = cartOpt.get();
			cart.getCartItems().clear();
			cartDao.saveCart(cart);
		}

		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setData("Cart cleared");
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Cart cleared successfully");
		structure.setHttpStatusCode(200);
		return structure;
	}

	// Helper method to build CartResponseDto
	private CartResponseDto buildCartResponseDto(Cart cart) {
		CartResponseDto dto = new CartResponseDto();
		dto.setCartId(cart.getCartId());
		dto.setUserId(cart.getUser().getUserId());
		dto.setUserName(cart.getUser().getName());

		List<CartResponseDto.CartItemDto> items = new ArrayList<>();
		double totalPrice = 0;
		int totalItems = 0;

		if (cart.getCartItems() != null) {
			for (CartItem item : cart.getCartItems()) {
				CartResponseDto.CartItemDto itemDto = new CartResponseDto.CartItemDto();
				itemDto.setCartItemId(item.getCartItemId());
				itemDto.setProductId(item.getProduct().getProductId());
				itemDto.setProductName(item.getProduct().getName());
				itemDto.setProductPrice(item.getProduct().getPrice());
				itemDto.setQuantity(item.getQuantity());
				double subtotal = item.getProduct().getPrice() * item.getQuantity();
				itemDto.setSubtotal(subtotal);
				items.add(itemDto);
				totalPrice += subtotal;
				totalItems += item.getQuantity();
			}
		}

		dto.setItems(items);
		dto.setTotalPrice(totalPrice);
		dto.setTotalItems(totalItems);
		return dto;
	}
}
