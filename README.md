# 🛒 E-Cart Application — Spring Boot REST API

A full-featured **E-Commerce REST API** built with Spring Boot that supports User Management, Product Catalog, Shopping Cart, Order Placement with Payment Processing, and Order Cancellation with Stock Restoration.

---

## 🏗️ Tech Stack

| Technology | Version |
|------------|---------|
| Java | 17+ |
| Spring Boot | 3.5.13 |
| Spring Data JPA | Hibernate ORM 6.x |
| MySQL | 8.0 |
| Lombok | Latest |
| ModelMapper | 3.2.4 |
| Swagger / OpenAPI | SpringDoc 2.8.16 |
| Build Tool | Maven |

---

## 📁 Project Structure

```
src/main/java/com/springboot/user_app/
├── UserAppApplication.java          # Main application + Swagger config
├── controller/
│   ├── UserController.java          # User CRUD endpoints
│   ├── ProductController.java       # Product CRUD endpoints
│   ├── CartController.java          # Shopping cart endpoints
│   └── OrderController.java         # Order & payment endpoints
├── entity/
│   ├── User.java                    # User entity
│   ├── Product.java                 # Product catalog entity (with stock)
│   ├── Cart.java                    # Shopping cart (1:1 with User)
│   ├── CartItem.java                # Cart items (product + quantity)
│   ├── Order.java                   # Order with status & payment info
│   └── OrderItem.java              # Ordered items (price snapshot)
├── service/
│   ├── UserService.java
│   ├── ProductService.java
│   ├── CartService.java
│   └── OrderService.java
├── dao/
│   ├── UserDao.java
│   ├── ProductDao.java
│   ├── CartDao.java
│   └── OrderDao.java
├── repository/
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   ├── CartRepository.java
│   ├── CartItemRepository.java
│   ├── OrderRepository.java
│   └── OrderItemRepository.java
├── dto/
│   ├── UserLoginRequestDto.java
│   ├── UserResponseDto.java
│   ├── AddToCartRequestDto.java
│   ├── UpdateCartItemRequestDto.java
│   ├── CartResponseDto.java
│   └── OrderResponseDto.java
├── customException/
│   ├── IdNotFoundException.java
│   ├── EmailNotFoundException.java
│   ├── IncorrectPasswordException.java
│   ├── ProductNotFoundException.java
│   ├── CartNotFoundException.java
│   ├── OrderNotFoundException.java
│   ├── InsufficientStockException.java
│   └── EmptyCartException.java
├── Exception/
│   ├── UserExceptionController.java
│   └── ProductExceptionController.java
└── response/
    └── ResponseStructure.java       # Generic API response wrapper
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 17 or higher
- MySQL 8.0 running on `localhost:3306`
- Maven (or use the included Maven Wrapper)

### Database Configuration

The application auto-creates the database. Update credentials in `application.properties` if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/springbootdb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=Vikram@123
```

### Run the Application

```bash
# Using Maven Wrapper
bash mvnw spring-boot:run

# Or with Maven installed
mvn spring-boot:run
```

The server starts on **http://localhost:8081**

### Swagger UI

Access the interactive API documentation at:
```
http://localhost:8081/swagger-ui.html
```

---

## 📊 Database Schema

```
┌──────────┐     ┌──────────┐     ┌───────────┐
│   User   │────<│  Orders  │────<│ OrderItem │
│          │     │          │     │           │
│ userId   │     │ orderId  │     │ orderItemId│
│ name     │     │ orderDate│     │ quantity   │
│ email    │     │ totalAmt │     │ price      │
│ password │     │ status   │     │           │>────┐
│ address  │     │ payment  │     └───────────┘     │
└──────────┘     └──────────┘                       │
      │                                             │
      │          ┌──────────┐     ┌───────────┐     │
      └─────────<│   Cart   │────<│ CartItem  │     │
                 │          │     │           │     │
                 │ cartId   │     │ cartItemId│     │
                 └──────────┘     │ quantity  │     │
                                  │           │>────┤
                                  └───────────┘     │
                                                    │
                                  ┌───────────┐     │
                                  │  Product  │<────┘
                                  │           │
                                  │ productId │
                                  │ name      │
                                  │ description│
                                  │ price     │
                                  │ stock     │
                                  │ type      │
                                  │ imageUrl  │
                                  └───────────┘
```

---

## 🔌 API Endpoints

### 👤 User Endpoints — `/user`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/user/register` | Register a new user | `{ "name", "email", "password", "address" }` |
| `POST` | `/user/userLogin` | User login | `{ "email", "password" }` |
| `GET` | `/user/getAllUser` | Get all users | — |
| `GET` | `/user/getUserById?UserId=` | Get user by ID | — |
| `PUT` | `/user/updateUser` | Update user details | `{ "userId", "name", "email", "password", "address" }` |
| `DELETE` | `/user/deleteUser/{uId}` | Delete a user | — |

### 📦 Product Endpoints — `/product`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/product/create` | Create a new product | `{ "name", "description", "price", "stock", "type", "imageUrl" }` |
| `GET` | `/product/getAllProduct` | Get all products | — |
| `GET` | `/product/getProductById?proId=` | Get product by ID | — |
| `GET` | `/product/getProductByName?name=` | Get product by name | — |
| `GET` | `/product/getProductByType?type=` | Get products by type | — |
| `PUT` | `/product/updateProduct` | Update a product | `{ "productId", "name", "description", "price", "stock", "type" }` |
| `DELETE` | `/product/deleteProduct/{proId}` | Delete a product | — |

### 🛒 Cart Endpoints — `/cart`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/cart/add` | Add product to cart | `{ "userId", "productId", "quantity" }` |
| `GET` | `/cart/{userId}` | View user's cart | — |
| `PUT` | `/cart/update` | Update cart item quantity | `{ "cartItemId", "quantity" }` |
| `DELETE` | `/cart/remove/{cartItemId}` | Remove item from cart | — |
| `DELETE` | `/cart/clear/{userId}` | Clear entire cart | — |

### 📋 Order Endpoints — `/order`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/order/place/{userId}` | Place order from cart (with payment) | — |
| `GET` | `/order/user/{userId}` | Get all orders for a user | — |
| `GET` | `/order/{orderId}` | Get order details by ID | — |
| `PUT` | `/order/cancel/{orderId}` | Cancel an order (restores stock) | — |

---

## 🔄 Order Placement Flow

```
1. User adds products to cart     →  POST /cart/add
2. User views cart with totals    →  GET  /cart/{userId}
3. User places order              →  POST /order/place/{userId}
   ├── Validates all cart items have sufficient stock
   ├── Simulates payment processing
   ├── Creates Order + OrderItems with price snapshots
   ├── Decrements product stock
   └── Clears the user's cart
4. User views order history       →  GET  /order/user/{userId}
5. User cancels order (optional)  →  PUT  /order/cancel/{orderId}
   ├── Restores product stock
   └── Sets status to CANCELLED / REFUNDED
```

---

## 📨 API Response Format

All endpoints return a consistent response structure:

```json
{
  "data": { ... },
  "time": "2026-04-13T10:11:12.876905",
  "message": "Order placed successfully! Payment: SUCCESS",
  "httpStatusCode": 201
}
```

### Sample — Place Order Response

```json
{
  "data": {
    "orderId": 1,
    "orderDate": "2026-04-13T10:11:12.869133",
    "totalAmount": 205997.0,
    "orderStatus": "PLACED",
    "paymentStatus": "SUCCESS",
    "userId": 1,
    "userName": "Vikram",
    "items": [
      {
        "orderItemId": 1,
        "productId": 1,
        "productName": "iPhone 15",
        "price": 79999.0,
        "quantity": 2,
        "subtotal": 159998.0
      },
      {
        "orderItemId": 2,
        "productId": 2,
        "productName": "Samsung TV",
        "price": 45999.0,
        "quantity": 1,
        "subtotal": 45999.0
      }
    ]
  },
  "time": "2026-04-13T10:11:12.876905",
  "message": "Order placed successfully! Payment: SUCCESS",
  "httpStatusCode": 201
}
```

---

## ⚠️ Error Handling

| Exception | HTTP Status | Scenario |
|-----------|-------------|----------|
| `IdNotFoundException` | 404 | User/Entity not found |
| `EmailNotFoundException` | 404 | Email not registered |
| `IncorrectPasswordException` | 401 | Wrong password on login |
| `ProductNotFoundException` | 404 | Product doesn't exist |
| `CartNotFoundException` | 404 | Cart not found for user |
| `OrderNotFoundException` | 404 | Order doesn't exist |
| `InsufficientStockException` | 400 | Not enough product stock |
| `EmptyCartException` | 400 | Placing order with empty cart |

---

## 👩‍💻 Author

**Deepthi Harini**

---

## 📄 License

This project is licensed under the Apache License 2.0.
