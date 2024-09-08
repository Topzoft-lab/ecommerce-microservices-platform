# Order Service

---

## **Overview**

The **Order Service** manages the entire order lifecycle, from order placement to payment processing and inventory updates. It integrates with payment gateways like Stripe or PayPal and communicates with the **Inventory Service** via RabbitMQ for real-time stock adjustments. This service is built using Go for its performance and concurrency capabilities. PostgreSQL is used for persistent order data storage.

---

## **Dependencies**

- **Go**: 1.17+
- **PostgreSQL**: Database for storing order details
- **Redis**: For caching order data and improving performance
- **RabbitMQ**: For communicating with the Inventory Service
- **Stripe/PayPal API**: For payment processing

---

## **API Endpoints**

### **1. Place an Order**

- **POST /orders**  
  **Description**: Allows a user to place an order by specifying the product and quantity. Payment details are also required.  
  **Request**: Requires `user_id`, `product_id`, `quantity`, and payment details (Stripe/PayPal).  
  **Response**: Returns a success message with the order ID and payment confirmation.  
  **Best Practices**:
  - Validates the user and product information to ensure they exist and are valid.
  - Communicates with the Payment Service for secure payment processing via Stripe or PayPal.
  - Implements input validation and sanitization to prevent SQL injection and data manipulation attacks.
  - Publishes an event to RabbitMQ to notify the Inventory Service to update stock.

---

### **2. View Order Details**

- **GET /orders/{order_id}**  
  **Description**: Retrieves the details of a specific order by its ID.  
  **Response**: Returns the order details, including the order status, product details, and payment information.  
  **Best Practices**:
  - Implements role-based access control (RBAC) to ensure that only the user who placed the order or an admin can view the details.
  - Caches order details in Redis for fast retrieval and reduced database load.
  - Implements rate limiting to prevent abuse.

---

### **3. Cancel an Order**

- **PUT /orders/{order_id}/cancel**  
  **Description**: Cancels an order that is pending or in progress. Updates the order status and restores inventory levels if necessary.  
  **Response**: Returns a success message once the order is canceled.  
  **Best Practices**:
  - Ensures the order can only be canceled if it hasn't been shipped or completed.
  - Updates the order status atomically to avoid race conditions.
  - Publishes an event to RabbitMQ to notify the Inventory Service to restore the stock.

---

### **4. Payment Confirmation (Event-Driven)**

- **POST /orders/payment-confirmation**  
  **Description**: Handles payment confirmation events from the Payment Service after a successful payment via Stripe or PayPal.  
  **Response**: Returns a success message upon successful confirmation.  
  **Best Practices**:
  - Verifies the payment details and updates the order status to "Completed."
  - Uses RabbitMQ to communicate the stock update to the Inventory Service.
  - Ensures idempotency to handle repeated or delayed payment confirmation events without affecting the order status.

---

## **Best Practices Implemented**

1. **Atomic Order Processing**:  
   - Order creation, status updates, and inventory adjustments are handled atomically to avoid race conditions or inconsistent states. Go’s concurrency features are used to manage these operations.

2. **Event-Driven Communication with RabbitMQ**:  
   - Order placement and cancellation events trigger stock updates in the **Inventory Service** through RabbitMQ. This decouples the services and ensures asynchronous, real-time stock management.

3. **Payment Integration with Stripe/PayPal**:  
   - The service securely communicates with Stripe or PayPal for processing payments. Webhooks or event-driven mechanisms are used for confirming payments and updating order statuses.

4. **Caching with Redis**:  
   - Frequently accessed order details are cached in Redis to improve performance and reduce database load. Time-to-live (TTL) settings ensure that cached data remains fresh.

5. **Role-Based Access Control (RBAC)**:  
   - Sensitive endpoints, such as viewing and canceling orders, are protected by role-based access control (RBAC) to ensure that only authorized users can perform these actions.

6. **Input Validation & Sanitization**:  
   - All inputs, including product IDs, user IDs, and payment details, are validated and sanitized to prevent SQL injection and other security vulnerabilities.

7. **Idempotency for Payment Events**:  
   - Payment events are handled idempotently, ensuring that repeated payment confirmations do not affect the order status, preventing double charges or incorrect updates.

8. **Rate Limiting**:  
   - Rate limiting is applied to critical endpoints (e.g., order placement) to prevent abuse and protect the service from Denial of Service (DoS) attacks.

---

## **How to Run the Service**

### **Prerequisites**

Make sure you have the following installed:
- Go 1.17+
- PostgreSQL (local or hosted)
- Redis (local or hosted)
- RabbitMQ (local or hosted)
- Stripe/PayPal developer account for payment integration

### **Configuration**

Update the configuration in `config/config.yaml` to match your database, Redis, RabbitMQ, and Stripe/PayPal setup:

```yaml
database:
  host: localhost
  port: 5432
  user: your_postgres_user
  password: your_postgres_password
  dbname: ecommerce_orders

redis:
  host: localhost
  port: 6379

rabbitmq:
  url: amqp://guest:guest@localhost:5672/

stripe:
  api_key: your_stripe_api_key

paypal:
  client_id: your_paypal_client_id
  client_secret: your_paypal_client_secret
