# Event Registration System — Spring Boot (Maven)
A Spring Boot backend service that supports user authentication (JWT) and event management (CRUD, registration).

## Features
- User registration & login with JWT-based authentication
- Secure APIs with Spring Security
- CRUD operations for events
- Event user registration with capacity checks
- PostgreSQL persistence with Spring Data JPA
- Unit and controller tests with JUnit + Mockito + MockMvc
- Global exception handling with clean JSON responses

## Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 13+ (running locally or via Docker)

## Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/manishchoudharylive/EventRegistrationSystem.git
cd EventRegistrationSystem
````

### 2. Configure PostgreSQL
Ensure PostgreSQL is running locally:

```postgresql
CREATE DATABASE new_db
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
```

Or use Docker:
```bash
docker run --name postgres-event -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=eventdb -p 5432:5432 -d postgres:13
```

### 3. Configure environment variables

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=eventdb
export DB_USER=postgres
export DB_PASS=***
export JWT_SECRET=my-very-secure-secret-key-32bytes
```

### 4. Build and run
```bash
mvn clean package
java -jar target/event-registration-0.0.1-SNAPSHOT.jar
```

The application starts on [http://localhost:8080](http://localhost:8080).

## Configuration
The application uses `application.yml`. Environment variables can override defaults.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:eventdb}
    username: ${DB_USER:postgres}
    password: ${DB_PASS:postgres}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

app:
  jwt:
    secret: ${JWT_SECRET} # 32-characters at least
    expiration-ms: 86400000 # 1 day

server:
  port: 8080
```

## API Usage

### Authentication

- **Register**: `POST /api/auth/register`
  ```json
  { "username": "alice", "password": "secret" }
  ```
- **Login**: `POST /api/auth/login`
  ```json
  { "username": "alice", "password": "secret" }
  ```
  Response:
  ```json
  { "token": "<jwt-token>" }
  ```

### Events (public api)
- `GET /api/events` – list events
- `GET /api/events/{id}` – get event by ID

### Events (secured)

All requests require header: `Authorization: Bearer <jwt-token>` and user has Admin access.
- `POST /api/events` – create event
- `PUT /api/events/{id}` – update event
- `DELETE /api/events/{id}` – delete event
- `POST /api/events/{id}/register` – register user for event

Example create event request:
```json
{
  "name": "Tech Conference 2025",
  "startDateTime": "2025-12-01T09:00:00",
  "location": "Bangalore",
  "capacity": 200
}
```

## Running Tests

```bash
mvn test
```

This runs unit and controller tests with JUnit, Mockito, and MockMvc.

## Production Notes

- **JWT Secret**: Use a secure, random 32+ byte string. Store in a vault (AWS Secrets Manager, HashiCorp Vault).
- **Database migrations**: Use Flyway or Liquibase instead of `ddl-auto=update`.
- **Logging**: Configure structured logs and integrate with ELK/EFK.
- **Dockerization**: Add a `Dockerfile` and optional `docker-compose.yml` for app + PostgreSQL.
- **Monitoring**: Integrate with Prometheus/Grafana.
- **Validation**: Annotate DTOs with `@NotBlank`, `@NotNull`, etc.
- **Documentation**: Add Swagger/OpenAPI via `springdoc-openapi`.

## Quick Start with Docker Compose

Create a `docker-compose.yml`:
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: eventdb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
  app:
    build: .
    depends_on:
      - postgres
    environment:
      DB_HOST: postgres
      DB_PORT: 5432
      DB_NAME: eventdb
      DB_USER: postgres
      DB_PASS: postgres
      JWT_SECRET: my-very-secure-secret-key-32bytes
    ports:
      - "8080:8080"
```

Run with:
```bash
docker-compose up --build
```

The app will be available at [http://localhost:8080](http://localhost:8080)
