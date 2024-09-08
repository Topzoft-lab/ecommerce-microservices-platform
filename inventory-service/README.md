# Inventory Service

---

## **Overview**

The **Inventory Service** manages real-time stock updates and availability for products. It listens for order-related events from RabbitMQ and adjusts inventory levels accordingly. This service is built using Go for high performance, PostgreSQL for persistent storage, and RabbitMQ for event-driven architecture. Redis is also used to cache inventory data for faster access when needed.

---

## **Dependencies**

- **Go**: 1.17+
- **PostgreSQL**: Database for storing inventory levels
- **Redis**: For caching inventory data
- **RabbitMQ**: For receiving and processing order events

---

## **API Endpoints**

### **1. Inventory Update**

- **PUT /inventory/update**  
  **Description**: Updates the inventory levels of a product after an order is placed or canceled.  
  **Request**: Requires the `product_id` and `quantity` to update.  
  **Response**: Returns a success message upon successful update.  
  **Best Practices**:
  - Validates the `product_id` and `quantity` to ensure valid inputs.
  - Uses Redis to cache inventory data and improve lookup times.
  - Ensures the update is atomic to avoid race conditions in multi-threaded environments.

---

### **2. Inventory Check**

- **GET /inventory/{product_id}**  
  **Description**: Retrieves the current stock level of a specific product.  
  **Response**: Returns the current stock quantity of the product.  
  **Best Practices**:
  - Caches the product's inventory in Redis to avoid unnecessary database hits.
  - Implements rate limiting to prevent abuse of the endpoint and overloading the system.
  - Ensures role-based access control (RBAC) to allow only authorized users to view inventory levels.

---

### **3. Inventory Reservation (Event-Driven)**

- **POST /inventory/reserve**  
  **Description**: Reserves inventory for a pending order. This is triggered when an order is placed but not yet completed.  
  **Response**: Returns a success message after reserving the inventory.  
  **Best Practices**:
  - Uses event-driven architecture with RabbitMQ to process order reservation events.
  - Ensures that the inventory reservation process is idempotent, meaning it can be called multiple times without unintended effects.
  - Automatically releases reserved inventory if the order is not completed within a predefined time window.

---

## **Best Practices Implemented**

1. **Caching with Redis**:  
   - Frequently accessed inventory data is cached in Redis, improving response times and reducing the load on PostgreSQL.
   - A time-to-live (TTL) is used to ensure cached inventory data stays up-to-date and is not stale.

2. **Event-Driven Architecture with RabbitMQ**:  
   - The service listens to events from RabbitMQ for order placement, cancellation, and stock adjustments, ensuring asynchronous communication between services.

3. **Atomic Operations for Inventory Updates**:  
   - Inventory updates are atomic, meaning that they are handled in such a way that data remains consistent even in multi-threaded or distributed environments.
   - Uses Go’s concurrency features and proper locking mechanisms to avoid race conditions during inventory updates.

4. **Input Validation & Sanitization**:  
   - All inputs, such as `product_id` and `quantity`, are validated and sanitized to prevent SQL injection and other vulnerabilities.

5. **Rate Limiting**:  
   - Rate limiting is applied to the inventory check endpoint to prevent abuse and protect the system from Denial of Service (DoS) attacks.

6. **Security and Access Control**:  
   - Access to sensitive endpoints (such as inventory levels) is restricted using role-based access control (RBAC) to ensure that only authorized users can modify or view stock levels.

7. **Idempotency for Reservation**:  
   - Ensures that inventory reservation is idempotent, meaning that the same request can be processed multiple times without side effects, preventing duplicate or conflicting updates.

---

## **How to Run the Service**

### **Prerequisites**

Make sure you have the following installed:
- Go 1.17+
- PostgreSQL (local or hosted)
- Redis (local or hosted)
- RabbitMQ (local or hosted)

### **Configuration**

Update the configuration in `config/config.yaml` to match your database, Redis, and RabbitMQ setup:

```yaml
database:
  host: localhost
  port: 5432
  user: your_postgres_user
  password: your_postgres_password
  dbname: ecommerce_inventory

redis:
  host: localhost
  port: 6379

rabbitmq:
  url: amqp://guest:guest@localhost:5672/
