package com.springboot.user_app.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.user_app.entity.Cart;
import com.springboot.user_app.entity.CartItem;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.repository.CartItemRepository;
import com.springboot.user_app.repository.CartRepository;

@Repository
public class CartDao {

	@Autowired
	CartRepository cartRepo;

	@Autowired
	CartItemRepository cartItemRepo;

	public Cart saveCart(Cart cart) {
		return cartRepo.save(cart);
	}

	public Optional<Cart> getCartByUser(User user) {
		return cartRepo.findByUser(user);
	}

	public Optional<Cart> getCartById(Long cartId) {
		return cartRepo.findById(cartId);
	}

	public CartItem saveCartItem(CartItem cartItem) {
		return cartItemRepo.save(cartItem);
	}

	public void deleteCartItem(Long cartItemId) {
		cartItemRepo.deleteById(cartItemId);
	}

	public Optional<CartItem> getCartItemById(Long cartItemId) {
		return cartItemRepo.findById(cartItemId);
	}

	public void deleteCart(Long cartId) {
		cartRepo.deleteById(cartId);
	}
}
