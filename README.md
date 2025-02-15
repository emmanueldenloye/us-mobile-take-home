# US Mobile Take-Home Assignment

This is a Spring Boot application for managing user data, billing cycles, and daily usage. It uses MongoDB as the database and is designed to run in a microservices environment.

## Features
- RESTful APIs for managing users, billing cycles, and daily usage.
- Seeded MongoDB database with realistic test data.
- Docker Compose setup for easy deployment.

## Technologies Used
- **Spring Boot**: Backend framework.
- **MongoDB**: Database for storing user, cycle, and usage data.
- **Docker**: Containerization for deployment.
- **Apache Commons Lang**: For generating random test data.

## Getting Started

### Prerequisites
- Java 17 or higher
- Docker and Docker Compose
- MongoDB (optional, if not using Docker)

### Running the Application

#### Option 1: Using Docker Compose
1. Clone the repository:
   ```bash
   git clone <your-repo-url>
   cd us-mobile
   docker-compose up --build
   ```
The mongo database should be seeded with some fake data.

### Design Decisions and Trade-Offs

#### Strongly-Typed Code
While the current implementation uses stringly-typed fields (e.g., `userId`, `mdn`) for simplicity and alignment with the provided schema, I recognize that this approach has limitations:
- **Lack of Compile-Time Safety**: Typos or incorrect field names can lead to runtime errors.
- **Reduced Readability**: String literals make the code harder to understand and maintain.

In a production environment, I would recommend the following improvements:
1. **Use Strongly-Typed DTOs**: Replace raw JSON strings with Java classes (e.g., `UserDTO`, `DailyUsageDTO`).
2. **Enums for Fixed Values**: Use enums for fields with a limited set of values (e.g., user roles, status codes).
3. **Constants for Field Names**: Define constants for frequently used strings to reduce the risk of typos.
4. **JSON Schema Validation**: Validate incoming and outgoing JSON against a schema to ensure data integrity.

#### Java Verbosity
Java’s verbosity can make development slower and more frustrating. To mitigate this, I would consider:
- **Using Lombok**: To reduce boilerplate code (e.g., getters, setters, constructors).
- **Exploring Kotlin**: As a more concise and expressive alternative to Java for future projects.