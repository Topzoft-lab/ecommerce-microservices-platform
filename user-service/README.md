# User Service

The **User Service** is responsible for managing user registration, authentication, and profile operations. It provides JWT-based authentication and supports OAuth login using third-party providers like Google and Facebook.

## Dependencies

- **Java**: JDK 11+
- **Spring Boot**: 2.6.2
- **PostgreSQL**: Database for user data
- **Redis**: For session management
- **JWT**: JSON Web Token for user authentication
- **OAuth2**: Integration with third-party login services

### API Endpoints

## **1. User Registration & Authentication**

- **POST /register**  
  **Description**: Registers a new user by accepting their email, password, and other optional details.  
  **Response**: Returns a success message upon successful registration.  
  **Best Practices**:
  - Uses input validation to ensure that email and password meet required standards.
  - Passwords are hashed using industry-standard encryption (e.g., BCrypt) before storage.

---

- **POST /login**  
  **Description**: Authenticates a user and generates a JWT token for subsequent API requests.  
  **Response**: Returns a JWT token on successful authentication.  
  **Best Practices**:
  - Secure password comparison with hashed passwords.
  - Generates short-lived JWT tokens with a refresh token mechanism for added security.
  - Includes rate limiting on login attempts to prevent brute-force attacks.

---

## **2. OAuth Authentication**

- **GET /oauth2/authorize/google**  
  **Description**: Redirects the user to the Google OAuth login page for third-party authentication.  
  **Response**: Redirect URL to Google OAuth page.  
  **Best Practices**:
  - Follows OAuth2 security guidelines and ensures proper callback URLs.
  - Secure token exchange and user validation through Google APIs.

---

- **GET /oauth2/callback/google**  
  **Description**: Handles the OAuth2 callback from Google, exchanges the authorization code for a JWT token.  
  **Response**: Returns a JWT token on successful authentication.  
  **Best Practices**:
  - Secure token exchange to prevent man-in-the-middle (MITM) attacks.
  - Session management using Redis to store OAuth tokens for enhanced security.

---

## **3. User Profile Management**

- **GET /user/profile**  
  **Description**: Retrieves the authenticated user's profile data (requires JWT).  
  **Response**: Returns the user's profile information (email, name, registration date).  
  **Best Practices**:
  - JWT-based authentication for secure access.
  - Implements fine-grained role-based access control (RBAC) to ensure users can only access their own profiles.

---

- **PUT /user/profile**  
  **Description**: Allows the authenticated user to update their profile information, such as name and password.  
  **Response**: Returns a success message after updating the profile.  
  **Best Practices**:
  - Only updates specified fields, and all input is sanitized to prevent injection attacks.
  - Password changes are handled securely with proper hashing and validation of current passwords.

---

## **4. Password Management**

- **PUT /user/change-password**  
  **Description**: Allows the authenticated user to change their password. Requires current and new passwords.  
  **Response**: Returns a success message if the password is updated.  
  **Best Practices**:
  - Password strength validation to ensure strong passwords.
  - Secure password comparison and proper handling of sensitive information.
  - Logs password change events for security auditing purposes.

---

- **POST /user/forgot-password**  
  **Description**: Sends a password reset email with a token to the user's email address.  
  **Response**: Success message indicating that the password reset email has been sent.  
  **Best Practices**:
  - Implements rate limiting on password reset requests to prevent abuse.
  - Securely generates a time-bound reset token.
  - Emails are sent using secure SMTP configurations, ensuring no sensitive data is leaked.

---

- **POST /user/reset-password**  
  **Description**: Resets the user's password using a valid reset token received via email.  
  **Response**: Success message confirming that the password has been reset.  
  **Best Practices**:
  - Secure token validation, ensuring that only valid, unexpired tokens are accepted.
  - Ensures that reset passwords follow strong password policies.

---

## **5. User Session & Logout**

- **POST /user/logout**  
  **Description**: Logs out the user by invalidating their JWT token (if using JWT session tokens).  
  **Response**: Returns a success message confirming that the user has logged out.  
  **Best Practices**:
  - Uses blacklisting or token invalidation techniques to prevent re-use of the JWT after logout.
  - Tracks session invalidation events for auditing purposes.

---

## **Best Practices Implemented**

1. **JWT Authentication**:  
   - Each user session is secured using a JWT token that is passed in the `Authorization` header for subsequent API requests.
   - Tokens are short-lived and can be refreshed using a separate refresh token for added security.

2. **Password Hashing**:  
   - User passwords are never stored in plain text. They are securely hashed using the **BCrypt** algorithm before being stored in the PostgreSQL database.

3. **Input Validation & Sanitization**:  
   - All user inputs, including email, password, and other profile fields, are validated and sanitized to prevent common vulnerabilities such as SQL injection and XSS (cross-site scripting).

4. **Rate Limiting**:  
   - Login and password recovery endpoints have rate limiting implemented to mitigate brute-force and dictionary attacks.

5. **OAuth2 Integration**:  
   - The service integrates OAuth2 for Google and Facebook login. This reduces the need for users to create new credentials and uses a secure, industry-standard method of authentication.

6. **Role-Based Access Control (RBAC)**:  
   - Users can only access resources they own. This is enforced through role-based access controls that verify user permissions for e


## How to Run the Service

### Prerequisites

Make sure you have the following installed:
- JDK 11+
- PostgreSQL (local or hosted)
- Redis (local or hosted)

### Configuration

Update the configuration in `src/main/resources/application.properties` to match your database and Redis setup:

```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password

# Redis Configuration
spring.redis.host=localhost
spring.redis.port=6379

# JWT Configuration
jwt.secret=your_jwt_secret

# OAuth Configuration
spring.security.oauth2.client.registration.google.client-id=your_google_client_id
spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret


