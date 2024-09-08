# Product Service

---

## **Overview**

The **Product Service** handles all product-related operations, including product listing, filtering, and recommendations. It provides APIs for retrieving product details, applying search filters, and serving recommendations based on user preferences. This service is built using Spring Boot and PostgreSQL, and it utilizes Redis for caching frequently accessed product information to improve performance and reduce database load.

---

## **Dependencies**

- **Java**: JDK 11+
- **Spring Boot**: 2.6.2
- **PostgreSQL**: Database for product data
- **Redis**: For caching product information
- **RabbitMQ**: For asynchronous communication with other services (e.g., Inventory Service)

---

## **API Endpoints**

### **1. Product Listing & Filtering**

- **GET /products**  
  **Description**: Retrieves a list of all available products, with optional filters for category, price range, and availability.  
  **Response**: Returns a paginated list of products based on the filters applied.  
  **Best Practices**:
  - Implements pagination to reduce load and prevent large data dumps.
  - Filters are applied at the database query level for efficiency.
  - Caches the product list in Redis to reduce repeated database queries.
  - Implements input validation to ensure that filters such as price and category are valid.

---

### **2. Product Details**

- **GET /products/{id}**  
  **Description**: Retrieves the detailed information of a specific product by its ID.  
  **Response**: Returns product details including name, description, price, stock availability, and category.  
  **Best Practices**:
  - Caches product details in Redis for quick retrieval.
  - Includes role-based access control (RBAC) to ensure that only authorized users (e.g., admins) can view restricted product details.
  - Ensures that product data is validated before returning to prevent sending incorrect or malicious data.

---

### **3. Product Recommendations**

- **GET /products/recommendations**  
  **Description**: Provides personalized product recommendations based on user preferences and purchase history.  
  **Response**: Returns a list of recommended products tailored to the authenticated user.  
  **Best Practices**:
  - Caches recommendations in Redis to improve response times.
  - Uses machine learning (optional) or rule-based algorithms to generate recommendations.
  - Validates user preferences to ensure recommendations are relevant and up-to-date.

---

## **Best Practices Implemented**

1. **Caching with Redis**:  
   - Product data such as lists and details are cached in Redis to reduce the load on the database and improve response times.
   - Time-to-live (TTL) settings are used to ensure that cached data is refreshed periodically to avoid serving stale data.

2. **Pagination for Product Listings**:  
   - Product listings are paginated to reduce the amount of data sent to the client at once. This improves performance and prevents overloading the server with large queries.

3. **Input Validation & Sanitization**:  
   - All input parameters for filtering and searching products are validated and sanitized to prevent SQL injection and other malicious attacks.

4. **Rate Limiting**:  
   - Rate limiting is applied to endpoints that could be susceptible to abuse, such as product searches, to prevent DoS (Denial of Service) attacks.

5. **Role-Based Access Control (RBAC)**:  
   - Certain product details or admin functionalities are only accessible to authorized users based on roles, ensuring that unauthorized users cannot access or modify sensitive information.

6. **Event-Driven Architecture with RabbitMQ**:  
   - RabbitMQ is used to communicate asynchronously with other services, such as sending updates to the **Inventory Service** when a product is created, updated, or deleted.

---

## **How to Run the Service**

### **Prerequisites**

Make sure you have the following installed:
- JDK 11+
- PostgreSQL (local or hosted)
- Redis (local or hosted)
- RabbitMQ (local or hosted)

### **Configuration**

Update the configuration in `src/main/resources/application.properties` to match your database and Redis setup:

```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=your_postgres_user
spring.datasource.password=your_postgres_password

# Redis Configuration
spring.redis.host=localhost
spring.redis.port=6379

# RabbitMQ Configuration
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
