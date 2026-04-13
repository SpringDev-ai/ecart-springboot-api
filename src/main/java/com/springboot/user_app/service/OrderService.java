package com.springboot.user_app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.user_app.customException.CartNotFoundException;
import com.springboot.user_app.customException.EmptyCartException;
import com.springboot.user_app.customException.IdNotFoundException;
import com.springboot.user_app.customException.InsufficientStockException;
import com.springboot.user_app.customException.OrderNotFoundException;
import com.springboot.user_app.dao.CartDao;
import com.springboot.user_app.dao.OrderDao;
import com.springboot.user_app.dao.ProductDao;
import com.springboot.user_app.dao.UserDao;
import com.springboot.user_app.dto.OrderResponseDto;
import com.springboot.user_app.entity.Cart;
import com.springboot.user_app.entity.CartItem;
import com.springboot.user_app.entity.Order;
import com.springboot.user_app.entity.OrderItem;
import com.springboot.user_app.entity.Product;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.response.ResponseStructure;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	OrderDao orderDao;

	@Autowired
	CartDao cartDao;

	@Autowired
	UserDao userDao;

	@Autowired
	ProductDao productDao;

	// Place order from cart — the main checkout flow
	@Transactional
	public ResponseStructure<OrderResponseDto> placeOrder(Long userId) {
		// 1. Validate user
		Optional<User> userOpt = userDao.getUserById(userId);
		if (!userOpt.isPresent()) {
			throw new IdNotFoundException("User not found with id: " + userId);
		}
		User user = userOpt.get();

		// 2. Get cart
		Cart cart = cartDao.getCartByUser(user)
				.orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

		if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
			throw new EmptyCartException("Cart is empty. Add items before placing an order.");
		}

		// 3. Validate stock for all items
		for (CartItem cartItem : cart.getCartItems()) {
			Product product = cartItem.getProduct();
			if (product.getStock() < cartItem.getQuantity()) {
				throw new InsufficientStockException(
						"Insufficient stock for product: " + product.getName()
								+ ". Available: " + product.getStock()
								+ ", Requested: " + cartItem.getQuantity());
			}
		}

		// 4. Calculate total
		double totalAmount = 0;
		for (CartItem cartItem : cart.getCartItems()) {
			totalAmount += cartItem.getProduct().getPrice() * cartItem.getQuantity();
		}

		// 5. Simulate payment (always success for now)
		boolean paymentSuccess = simulatePayment(totalAmount);

		// 6. Create order
		Order order = new Order();
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		order.setTotalAmount(totalAmount);
		order.setOrderStatus("PLACED");
		order.setPaymentStatus(paymentSuccess ? "SUCCESS" : "FAILED");
		order.setOrderItems(new ArrayList<>());

		Order savedOrder = orderDao.saveOrder(order);

		// 7. Create order items from cart items
		for (CartItem cartItem : cart.getCartItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(savedOrder);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPrice(cartItem.getProduct().getPrice());
			savedOrder.getOrderItems().add(orderItem);

			// 8. Decrement product stock
			Product product = cartItem.getProduct();
			product.setStock(product.getStock() - cartItem.getQuantity());
			productDao.updateProduct(product);
		}

		// Save order with items
		savedOrder = orderDao.saveOrder(savedOrder);

		// 9. Clear the cart
		cart.getCartItems().clear();
		cartDao.saveCart(cart);

		// 10. Build response
		OrderResponseDto responseDto = buildOrderResponseDto(savedOrder);
		ResponseStructure<OrderResponseDto> structure = new ResponseStructure<>();
		structure.setData(responseDto);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Order placed successfully! Payment: " + (paymentSuccess ? "SUCCESS" : "FAILED"));
		structure.setHttpStatusCode(201);
		return structure;
	}

	// Get all orders for a user
	public ResponseStructure<List<OrderResponseDto>> getOrdersByUser(Long userId) {
		Optional<User> userOpt = userDao.getUserById(userId);
		if (!userOpt.isPresent()) {
			throw new IdNotFoundException("User not found with id: " + userId);
		}
		User user = userOpt.get();

		List<Order> orders = orderDao.getOrdersByUser(user);
		List<OrderResponseDto> orderDtos = new ArrayList<>();
		for (Order order : orders) {
			orderDtos.add(buildOrderResponseDto(order));
		}

		ResponseStructure<List<OrderResponseDto>> structure = new ResponseStructure<>();
		structure.setData(orderDtos);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Orders retrieved successfully");
		structure.setHttpStatusCode(200);
		return structure;
	}

	// Get order by ID
	public ResponseStructure<OrderResponseDto> getOrderById(Long orderId) {
		Optional<Order> orderOpt = orderDao.getOrderById(orderId);
		if (!orderOpt.isPresent()) {
			throw new OrderNotFoundException("Order not found with id: " + orderId);
		}
		Order order = orderOpt.get();

		OrderResponseDto responseDto = buildOrderResponseDto(order);
		ResponseStructure<OrderResponseDto> structure = new ResponseStructure<>();
		structure.setData(responseDto);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Order retrieved successfully");
		structure.setHttpStatusCode(200);
		return structure;
	}

	// Cancel order — restores stock
	@Transactional
	public ResponseStructure<OrderResponseDto> cancelOrder(Long orderId) {
		Optional<Order> orderOpt = orderDao.getOrderById(orderId);
		if (!orderOpt.isPresent()) {
			throw new OrderNotFoundException("Order not found with id: " + orderId);
		}
		Order order = orderOpt.get();

		if ("CANCELLED".equals(order.getOrderStatus())) {
			throw new OrderNotFoundException("Order is already cancelled");
		}

		if ("DELIVERED".equals(order.getOrderStatus())) {
			throw new OrderNotFoundException("Cannot cancel a delivered order");
		}

		// Restore product stock
		for (OrderItem orderItem : order.getOrderItems()) {
			Product product = orderItem.getProduct();
			product.setStock(product.getStock() + orderItem.getQuantity());
			productDao.updateProduct(product);
		}

		order.setOrderStatus("CANCELLED");
		order.setPaymentStatus("REFUNDED");
		Order updatedOrder = orderDao.saveOrder(order);

		OrderResponseDto responseDto = buildOrderResponseDto(updatedOrder);
		ResponseStructure<OrderResponseDto> structure = new ResponseStructure<>();
		structure.setData(responseDto);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Order cancelled successfully. Stock restored.");
		structure.setHttpStatusCode(200);
		return structure;
	}

	// Simulate payment — always returns true for now
	private boolean simulatePayment(double amount) {
		// In a real application, this would integrate with a payment gateway
		// For now, we simulate a successful payment
		System.out.println("Processing payment of ₹" + amount + "...");
		System.out.println("Payment successful!");
		return true;
	}

	// Helper to build OrderResponseDto
	private OrderResponseDto buildOrderResponseDto(Order order) {
		OrderResponseDto dto = new OrderResponseDto();
		dto.setOrderId(order.getOrderId());
		dto.setOrderDate(order.getOrderDate());
		dto.setTotalAmount(order.getTotalAmount());
		dto.setOrderStatus(order.getOrderStatus());
		dto.setPaymentStatus(order.getPaymentStatus());
		dto.setUserId(order.getUser().getUserId());
		dto.setUserName(order.getUser().getName());

		List<OrderResponseDto.OrderItemDto> items = new ArrayList<>();
		if (order.getOrderItems() != null) {
			for (OrderItem item : order.getOrderItems()) {
				OrderResponseDto.OrderItemDto itemDto = new OrderResponseDto.OrderItemDto();
				itemDto.setOrderItemId(item.getOrderItemId());
				itemDto.setProductId(item.getProduct().getProductId());
				itemDto.setProductName(item.getProduct().getName());
				itemDto.setPrice(item.getPrice());
				itemDto.setQuantity(item.getQuantity());
				itemDto.setSubtotal(item.getPrice() * item.getQuantity());
				items.add(itemDto);
			}
		}
		dto.setItems(items);
		return dto;
	}
}
