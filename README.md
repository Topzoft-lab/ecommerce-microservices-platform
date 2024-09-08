# E-Commerce Platform with Microservices

A scalable e-commerce platform built using microservices architecture with Java, Go, Docker, Kubernetes, and Redis. This platform demonstrates real-time inventory updates, product recommendations, and third-party payment integrations.

## Project Overview

This project is an e-commerce platform designed to handle large-scale traffic with microservices architecture. It features:

- Product listing, filtering, and recommendations.
- Real-time inventory updates using event-driven architecture with RabbitMQ.
- Secure user authentication with JWT and OAuth (Google/Facebook).
- Payment integration with Stripe and PayPal.
- Caching frequent queries with Redis to enhance performance.
- Microservices are containerized with Docker and deployed on Kubernetes.

This project demonstrates best practices in microservices architecture, performance optimization, and scalability.

## System Architecture

The platform is divided into multiple microservices, each handling specific functionality:

- **User Service**: Manages user authentication and profiles (Java - Spring Boot).
- **Product Service**: Handles product listing, filtering, and recommendations (Java - Spring Boot).
- **Inventory Service**: Manages real-time stock updates (Go).
- **Order Service**: Handles order placement and payment processing (Go).
- **Payment Service**: Integrates with Stripe/PayPal for secure transactions (Java/Go).
- **Recommendation Service**: Provides product recommendations (Java - Spring Boot).
  
All services communicate asynchronously using RabbitMQ for event-driven architecture.

## Installation

### Prerequisites
- Docker and Docker Compose
- Kubernetes (Minikube or any Kubernetes provider)
- PostgreSQL
- Redis
- RabbitMQ

### Steps to Run Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ecommerce-microservices-platform.git
   cd ecommerce-microservices-platform
2. docker-compose up --build
3. Access the services at:
- localhost:8080 for User Service
- localhost:8081 for Product Service
- localhost:8082 for Inventory Service
- localhost:8083 for Order Service
- localhost:8084 for Payment Service
- localhost:8085 for Recommendation Service


#### **6. API Documentation**

## API Endpoints

### User Service
- `POST /register`: Register a new user.
- `POST /login`: Login and receive JWT.
- `GET /user/profile`: Retrieve user profile.

### Product Service
- `GET /products`: List all products.
- `GET /products/{id}`: Get product details by ID.
- `GET /products/recommendations`: Get recommended products.

### Order Service
- `POST /orders`: Create a new order.
- `GET /orders/{id}`: Get order details by ID.

### Inventory Service
- Listens to order events and updates product stock.

## Database Choices

The following table outlines the database choices for each microservice, along with the reasoning behind the selection:

| **Service**              | **Database Choice**          | **Reason**                                                                 |
|--------------------------|------------------------------|-----------------------------------------------------------------------------|
| **User Service**          | **PostgreSQL**               | Relational data, ACID compliance, secure handling of user profiles          |
| **Product Service**       | **PostgreSQL**               | Structured data, full-text search, relational integrity                     |
| **Inventory Service**     | **PostgreSQL**               | Real-time stock updates, transaction-intensive operations                   |
| **Order Service**         | **PostgreSQL**               | Transactional data, relational model, complex queries                       |
| **Payment Service**       | **PostgreSQL**               | Sensitive financial data, strong consistency, transactional integrity       |
| **Recommendation Service** (Optional) | **MongoDB** (Primary) / **Redis** (Cache) | Flexible document storage, high scalability, fast reads using Redis caching |


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

