# Use an official Maven image to run Maven commands
FROM maven:3.9.2-eclipse-temurin-17 as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project into the container
COPY . .

# Install project dependencies and build the project in development mode
RUN mvn clean install -DskipTests

# Now use a lightweight JDK image for runtime
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=builder /app/target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]

# Expose the application port (ensure this matches your application properties)
EXPOSE 8081
