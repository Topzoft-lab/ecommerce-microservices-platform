# Stage 1: Build the Go application
FROM golang:1.18-alpine AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy go.mod and go.sum files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy the entire project into the container
COPY . .

# Build the application binary
RUN go build -o order-service .

# Stage 2: Run the Go application in a lightweight image
FROM alpine:latest

# Set the working directory inside the container
WORKDIR /app

# Copy the binary from the builder stage
COPY --from=builder /app/order-service .

# Expose the port the service will run on
EXPOSE 8084

# Command to run the service
ENTRYPOINT ["./order-service"]
