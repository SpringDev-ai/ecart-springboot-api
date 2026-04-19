# Spring Security Help

This project now uses a simple Spring Security setup that is close to common real-world REST API practice, but still easy to learn:

- Passwords are stored as `BCrypt` hashes, not plain text.
- Authentication uses Spring Security with a database-backed `UserDetailsService`.
- The API is stateless, so there is no server-side session.
- Protected APIs use HTTP Basic authentication.
- Public APIs stay open for registration, login, Swagger, and product read operations.

## 1. What was added

### Dependency

`pom.xml`

- Added `spring-boot-starter-security`

### Security classes

`src/main/java/com/springboot/user_app/security`

- `SecurityConfig`
  Configures public vs protected endpoints, stateless session policy, HTTP Basic, password encoder, and authentication manager.
- `ApplicationUserDetailsService`
  Loads a user from the database by email for Spring Security.
- `CustomAuthenticationEntryPoint`
  Returns a structured JSON response when authentication is missing or invalid.
- `CustomAccessDeniedHandler`
  Returns a structured JSON response when a logged-in user does not have enough permission.

### User model changes

`User`

- Added `role`
- Added a unique constraint on `email`
- Password is now expected to be stored in encoded form

## 2. Security flow

### Registration flow

1. Client calls `POST /user/register`
2. User sends `name`, `email`, `password`, and optional `address`
3. `UserService.registeruser()` encodes the password with `BCrypt`
4. User is saved in MySQL
5. Password hash is stored in the database

Result:

- Passwords are never stored as plain text

### Login flow

1. Client calls `POST /user/userLogin`
2. Request body contains `email` and `password`
3. `AuthenticationManager` checks the credentials using:
   `ApplicationUserDetailsService` + `PasswordEncoder`
4. If credentials are valid, the API returns a success response

Important:

- This login endpoint does not create a token
- It only verifies credentials
- For protected endpoints, the client must still send Basic Auth on each request

### Protected request flow

1. Client calls a protected endpoint like `GET /cart/{userId}`
2. Client sends:
   `Authorization: Basic base64(email:password)`
3. Spring Security reads the header
4. `ApplicationUserDetailsService` loads the user from DB
5. Spring compares the submitted password with the stored `BCrypt` hash
6. If valid, request continues to the controller
7. If invalid or missing, a `401 Unauthorized` JSON response is returned

## 3. Which endpoints are public

These endpoints are public:

- `POST /user/register`
- `POST /user/userLogin`
- `/v3/api-docs/**`
- `/swagger-ui/**`
- `/swagger-ui.html`
- All `GET` endpoints under `/product/**`

Everything else requires authentication.

## 4. Why this is considered a better practice

Compared to the old implementation:

- Old: password compared with plain string equality
- New: password verified through Spring Security + `BCrypt`

- Old: no framework-managed authentication
- New: Spring Security handles authentication centrally

- Old: no standard protection for routes
- New: routes are protected through `SecurityFilterChain`

- Old: plain text password storage risk
- New: hashed password storage

## 5. Example requests

### Register

```http
POST /user/register
Content-Type: application/json

{
  "name": "Rahul",
  "email": "rahul@gmail.com",
  "password": "Rahul@123",
  "address": "Bangalore"
}
```

### Login check

```http
POST /user/userLogin
Content-Type: application/json

{
  "email": "rahul@gmail.com",
  "password": "Rahul@123"
}
```

### Call a protected API with Basic Auth

```http
GET /cart/1
Authorization: Basic cmFodWxAZ21haWwuY29tOlJhaHVsQDEyMw==
```

That Base64 value represents:

```text
rahul@gmail.com:Rahul@123
```

## 6. How Spring Security matches the password

Suppose the database stores:

```text
$2a$10$....
```

This is a `BCrypt` hash.

When the user logs in:

1. User enters plain password
2. Spring Security takes that plain password
3. `BCryptPasswordEncoder.matches()` compares it with the stored hash
4. If matched, authentication succeeds

So:

- plain password is used only during request time
- hashed password is stored in DB

## 7. Role field

The project now has:

- `USER`
- `ADMIN`

Right now the configuration keeps authorization simple and only requires authentication for protected routes.

That means:

- role data is ready for future learning
- later you can restrict endpoints with `hasRole("ADMIN")`

Example future idea:

- only `ADMIN` can create or delete products
- only `USER` or `ADMIN` can manage carts and orders

## 8. Files to study in order

If you want to learn the flow step by step, read in this order:

1. `src/main/java/com/springboot/user_app/security/SecurityConfig.java`
2. `src/main/java/com/springboot/user_app/security/ApplicationUserDetailsService.java`
3. `src/main/java/com/springboot/user_app/service/UserService.java`
4. `src/main/java/com/springboot/user_app/entity/User.java`
5. `src/main/java/com/springboot/user_app/controller/UserController.java`

## 9. Important limitation to know

This setup is intentionally simple for learning.

In larger production systems, teams often move from Basic Auth to:

- JWT authentication
- refresh tokens
- role-based authorization rules
- ownership checks so one user cannot access another user's cart or order data

For this project, the current setup is a good beginner-friendly foundation.
