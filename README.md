# Insurance Management System

A full-stack insurance management application built with Spring Boot 4, React, and MySQL. Supports account registration, JWT authentication, policy management, and claims tracking.

---

## Tech Stack

**Backend**
- Java 21 + Spring Boot 4
- Spring Security (JWT + Basic Auth)
- Spring Data JPA / Hibernate
- MySQL 8
- Virtual Threads (Project Loom)
- SpringDoc OpenAPI (Swagger UI)

**Frontend**
- React 18 + Vite
- React Router v6
- Axios
- Served via nginx in production

**Infrastructure**
- Docker + Docker Compose

---

## Project Structure

```
.
├── src/                        # Spring Boot application
│   ├── main/java/com/api/demo/
│   │   ├── config/             # Security, JWT, CORS, virtual threads
│   │   ├── controller/         # REST controllers
│   │   ├── dto/                # Data transfer objects
│   │   ├── entity/             # JPA entities
│   │   ├── exception/          # Global exception handling
│   │   ├── model/              # Enums and response models
│   │   ├── repository/         # Spring Data repositories
│   │   └── service/            # Business logic
│   └── test/                   # JUnit 5 test suite
├── demo-ui/                    # React frontend
│   ├── src/
│   │   ├── api/                # Axios API client
│   │   ├── context/            # Auth context (JWT storage)
│   │   └── pages/              # Login, Register, Accounts, Policies, Claims
│   ├── Dockerfile
│   └── nginx.conf
├── Dockerfile                  # Backend Docker build
└── docker-compose.yml          # Full stack orchestration
```

---

## Running with Docker (recommended)

Requires [Docker Desktop](https://www.docker.com/products/docker-desktop/) to be running.

```bash
docker-compose up --build
```

| Service  | URL                       |
|----------|---------------------------|
| UI       | http://localhost:3000     |
| API      | http://localhost:8080     |
| Swagger  | http://localhost:8080/swagger-ui/index.html |
| MySQL    | localhost:3306            |

To stop:
```bash
docker-compose down
```

To stop and wipe the database:
```bash
docker-compose down -v
```

---

## Running Locally

### Prerequisites
- Java 21
- Maven 3.9+
- MySQL 8 running locally with a database named `insurance`
- Node.js 20+

### Backend

```bash
# Create the database first
mysql -u root -p -e "CREATE DATABASE insurance;"

# Run the app
mvn spring-boot:run
```

The API starts on `http://localhost:8080`.

Override DB credentials if needed:
```bash
DB_HOST=localhost DB_PORT=3306 DB_NAME=insurance DB_USERNAME=root DB_PASSWORD=yourpassword mvn spring-boot:run
```

### Frontend

```bash
cd demo-ui
npm install
npm run dev
```

The UI starts on `http://localhost:5173` and proxies API calls to `localhost:8080`.

---

## Authentication Flow

This app uses **HTTP Basic Auth** to sign in and receive a **JWT** for subsequent requests.

### 1. Register
```
POST /register
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "Password1@",
  "phoneNumber": "1234567890",
  "about": "Test user",
  "dateOfBirth": "1990-01-01"
}
```

Password rules: min 8 chars, must include uppercase, lowercase, number, and special character.

### 2. Login (get JWT)
```
GET /login
Authorization: Basic <base64(email:password)>
```

The JWT is returned in the `Authorization` response header.

### 3. Use JWT on protected endpoints
```
GET /api/accounts/
Authorization: Bearer <token>
```

---

## API Reference

### Public
| Method | Endpoint    | Description         | Auth     |
|--------|-------------|---------------------|----------|
| POST   | /register   | Create account      | None     |
| GET    | /login      | Sign in, get JWT    | Basic    |
| GET    | /welcome    | Health check        | None     |

### Accounts
| Method | Endpoint              | Description          | Auth   |
|--------|-----------------------|----------------------|--------|
| GET    | /api/accounts/        | List all accounts    | JWT    |
| GET    | /api/accounts/{id}    | Get account by ID    | JWT    |
| GET    | /api/accounts/email/  | Get by email         | JWT    |
| PUT    | /api/accounts/{id}    | Update account       | JWT    |
| DELETE | /api/accounts/{id}    | Delete account       | JWT    |

### Policies
| Method | Endpoint                  | Description          | Auth   |
|--------|---------------------------|----------------------|--------|
| GET    | /api/policies/            | List all policies    | JWT    |
| GET    | /api/policies/{id}        | Get policy by ID     | JWT    |
| POST   | /api/policies/{accountId} | Create policy        | JWT    |
| PUT    | /api/policies/{id}        | Update policy        | JWT    |
| DELETE | /api/policies/{id}        | Delete policy        | JWT    |

### Claims
| Method | Endpoint               | Description          | Auth   |
|--------|------------------------|----------------------|--------|
| GET    | /api/claims/           | List all claims      | JWT    |
| GET    | /api/claims/{id}       | Get claim by ID      | JWT    |
| POST   | /api/claims/{policyId} | Create claim         | JWT    |
| PUT    | /api/claims/{id}       | Update claim         | JWT    |
| DELETE | /api/claims/{id}       | Delete claim         | JWT    |

Claim statuses: `SUBMITTED`, `IN_PROGRESS`, `APPROVED`, `DENIED`

---

## Running Tests

```bash
mvn test
```

The test suite uses H2 in-memory database — no MySQL required.

Tests cover:
- Service layer unit tests (Mockito) for Account, Policy, and Claim
- Controller unit tests (direct invocation) for all four controllers

---

## Configuration

All backend config is in `src/main/resources/application.properties`.

| Property | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | `localhost` | MySQL host |
| `DB_PORT` | `3306` | MySQL port |
| `DB_NAME` | `insurance` | Database name |
| `DB_USERNAME` | `root` | DB username |
| `DB_PASSWORD` | `root` | DB password |
| `spring.threads.virtual.enabled` | `true` | Enable Java 21 virtual threads |
| `spring.jpa.hibernate.ddl-auto` | `update` | Schema strategy |

---

## Virtual Threads

The app uses Java 21 virtual threads (Project Loom) for improved concurrency under load:

- `spring.threads.virtual.enabled=true` — Tomcat dispatches each HTTP request on a virtual thread
- `VirtualThreadConfig` bean — Spring's async executor also uses virtual threads

This allows the app to handle significantly more concurrent requests without increasing platform thread count, particularly beneficial for the blocking JPA/MySQL I/O calls.
