# Insurance Management System

A full-stack insurance management application built with Spring Boot 4, React, and MySQL. Supports account registration, JWT authentication, policy management, claims tracking, and insurance line management.

---

## Tech Stack

**Backend**
- Java 21 + Spring Boot 4
- Spring Security (JWT + Basic Auth)
- Spring Data JPA / Hibernate
- MySQL 8
- Virtual Threads (Project Loom)
- SpringDoc OpenAPI (Swagger UI)
- Spotless (code formatting with Eclipse formatter)

**Frontend**
- React 18 + Vite
- React Router v6
- Axios
- Served via nginx in production

**Infrastructure**
- Docker + Docker Compose
- MySQL initialization scripts

---

## Project Structure

```
.
├── demo-api/                   # Spring Boot application
│   ├── src/main/java/com/api/demo/
│   │   ├── config/             # Security, JWT, CORS, virtual threads
│   │   ├── controller/         # REST controllers (Account, Policy, Claim, Line)
│   │   ├── dto/                # Data transfer objects
│   │   ├── entity/             # JPA entities
│   │   ├── exception/          # Global exception handling
│   │   ├── model/              # Enums and response models
│   │   ├── repository/         # Spring Data repositories
│   │   └── service/            # Business logic
│   ├── src/test/               # JUnit 5 test suite
│   └── pom.xml                 # Maven dependencies + Spotless plugin
├── demo-ui/                    # React frontend
│   ├── src/
│   │   ├── api/                # Axios API client
│   │   ├── context/            # Auth context (JWT storage)
│   │   └── pages/              # Home, Login, Register, Accounts, Policies, Claims, Lines
│   ├── Dockerfile
│   └── nginx.conf
├── docker/
│   └── init.sql                # MySQL schema + seed data
├── Dockerfile                  # Backend Docker build
├── docker-compose.yml          # Full stack orchestration
└── .git/hooks/pre-commit       # Auto-format code with Spotless
```

---

## Running with Docker (recommended)

Requires [Docker Desktop](https://www.docker.com/products/docker-desktop/) to be running.

### Production Mode (default)

Builds and serves the UI with nginx:

```bash
docker-compose up --build
```

| Service  | URL                       |
|----------|---------------------------|
| UI       | http://localhost:3000     |
| API      | http://localhost:8080     |
| Swagger  | http://localhost:8080/swagger-ui/index.html |
| MySQL    | localhost:3306            |

### Development Mode (with hot-reload)

Runs Vite dev server with hot module replacement for frontend changes:

```bash
docker-compose --profile dev up --build
```

| Service  | URL                       |
|----------|---------------------------|
| UI (dev) | http://localhost:5173     |
| API      | http://localhost:8080     |
| Swagger  | http://localhost:8080/swagger-ui/index.html |
| MySQL    | localhost:3306            |

In dev mode, any changes to `demo-ui/src/**` files will automatically reload in the browser.

The MySQL database is automatically initialized with:
- Schema for all tables (account, policy, claim, line)
- Seed data for 4 insurance lines (Auto, Home, Life, Health)

To stop:
```bash
docker-compose down
# or for dev mode
docker-compose --profile dev down
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

# Optionally run the init script
mysql -u root -p insurance < docker/init.sql

# Run the app
cd demo-api
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

Override DB credentials if needed:
```bash
DB_HOST=localhost DB_PORT=3306 DB_NAME=insurance DB_USERNAME=root DB_PASSWORD=yourpassword ./mvnw spring-boot:run
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

### Lines
| Method | Endpoint           | Description          | Auth   |
|--------|--------------------|----------------------|--------|
| GET    | /api/lines/        | List all lines       | JWT    |
| GET    | /api/lines/{id}    | Get line by ID       | JWT    |
| POST   | /api/lines/        | Create line          | JWT    |
| PUT    | /api/lines/{id}    | Update line          | JWT    |
| DELETE | /api/lines/{id}    | Delete line          | JWT    |

Lines represent insurance product types (Auto, Home, Health, Life) with coverage ranges.

---

## Running Tests

```bash
cd demo-api
./mvnw test
```

The test suite uses H2 in-memory database — no MySQL required.

Tests cover:
- Service layer unit tests (Mockito) for Account, Policy, Claim, and Line
- Controller unit tests (direct invocation) for all controllers

---

## Code Formatting

### Backend (Java)

The project uses **Spotless** with Eclipse formatter to enforce consistent code style.

#### Format code manually
```bash
cd demo-api
./mvnw spotless:apply
```

#### Check formatting
```bash
cd demo-api
./mvnw spotless:check
```

### Frontend (JavaScript/React)

The project uses **ESLint** with React plugins for code quality and consistency.

#### Lint code manually
```bash
cd demo-ui
npm run lint
```

#### Auto-fix linting issues
```bash
cd demo-ui
npm run lint:fix
```

### Pre-commit hook
A git pre-commit hook automatically formats and lints all code before every commit:
- Located at `.git/hooks/pre-commit`
- Runs `mvn spotless:apply` for Java files
- Runs `npm run lint:fix` for JavaScript/React files
- Re-stages formatted files

This ensures no unformatted or linting-error code ever touches the repository.

---

## Configuration

All backend config is in `demo-api/src/main/resources/application.properties`.

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

---

## UI Features

- **Home Page**: Landing page with hero section, feature cards, and CTAs
- **Authentication**: Login/Register with JWT token management
- **Accounts**: View and manage user accounts
- **Policies**: Create and manage insurance policies
- **Claims**: File and track insurance claims
- **Lines**: Manage insurance product lines (Auto, Home, Health, Life)

The "Insurance Management" title in the nav bar links back to the home page.
