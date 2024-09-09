# Payment Service

---

## **Overview**

The **Payment Service** is responsible for handling payment processing via third-party providers such as Stripe and PayPal. It securely processes payments, confirms transactions, and communicates with other services, such as the **Order Service**, via RabbitMQ to update the order status and inventory. This service is built using Go for performance, with RabbitMQ for event-driven communication and PostgreSQL for storing payment records.

---

## **Dependencies**

- **Java**: JDK 11+
- **PostgreSQL**: Database for storing payment records
- **RabbitMQ**: For communicating payment events with other services (e.g., Order Service)
- **Stripe/PayPal API**: For processing payments

---

## **API Endpoints**

### **1. Process Payment**

- **POST /payment/process**  
  **Description**: Processes a payment by accepting order details and payment method (Stripe/PayPal).  
  **Request**: Requires `order_id`, `user_id`, `amount`, and `payment_method` (Stripe/PayPal).  
  **Response**: Returns a payment confirmation with transaction details.  
  **Best Practices**:
  - Implements input validation to ensure that all required fields are provided and valid.
  - Securely communicates with Stripe/PayPal using API keys stored in environment variables.
  - Publishes a payment confirmation event to RabbitMQ, which is consumed by the **Order Service** to update the order status.

---

### **2. Payment Status Check**

- **GET /payment/{payment_id}**  
  **Description**: Retrieves the current status of a specific payment by its ID.  
  **Response**: Returns the status of the payment (e.g., "Pending", "Completed", "Failed").  
  **Best Practices**:
  - Caches payment status in Redis to improve lookup times for frequently accessed payment records.
  - Implements role-based access control (RBAC) to ensure that only authorized users (admins or the user who initiated the payment) can check the status.

---

### **3. Payment Webhook for Stripe/PayPal**

- **POST /payment/webhook/stripe**  
  **Description**: Handles payment confirmation events sent by Stripe's webhook system after a successful or failed payment.  
  **Response**: Returns a success message once the payment event is processed.  
  **Best Practices**:
  - Verifies the webhook signature to ensure that the request comes from Stripe.
  - Updates the payment status and publishes an event to RabbitMQ to notify the **Order Service**.
  - Ensures idempotency by tracking processed webhook events to avoid processing the same event multiple times.

---

## **Best Practices Implemented**

1. **Secure Payment Processing**:  
   - Payment requests are securely communicated with Stripe or PayPal using environment-stored API keys. These keys are never exposed in code or logs, and all communication with payment providers uses HTTPS.

2. **Webhook Signature Validation**:  
   - The service validates webhook signatures from Stripe and PayPal to ensure that payment events are authentic and have not been tampered with.

3. **Event-Driven Communication with RabbitMQ**:  
   - Payment events (e.g., success, failure) are published to RabbitMQ and consumed by the **Order Service** to update the order status and the **Inventory Service** to adjust stock levels accordingly.

4. **Idempotency for Payment Webhooks**:  
   - Payment webhook events are processed idempotently, meaning that duplicate or repeated webhook events do not affect the payment status or order lifecycle.

5. **Input Validation & Sanitization**:  
   - All input data, including order IDs, user IDs, and payment details, is validated and sanitized to prevent injection attacks and invalid data from being processed.

6. **Caching with Redis**:  
   - Frequently accessed payment records (e.g., status checks) are cached in Redis to reduce the load on the PostgreSQL database and improve response times.

7. **Role-Based Access Control (RBAC)**:  
   - Access to sensitive endpoints, such as payment status checks, is protected by role-based access control (RBAC) to ensure that only authorized users can view or manage payment details.

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

Update the configuration in `config/config.yaml` to match your database, Redis, RabbitMQ, Stripe, and PayPal setup:

```yaml
database:
  host: localhost
  port: 5432
  user: your_postgres_user
  password: your_postgres_password
  dbname: ecommerce_payments

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
