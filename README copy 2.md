# Insurance Management System

A modern, full-stack insurance management application built with Spring Boot and React. This system provides comprehensive APIs for managing insurance accounts, policies, claims, and coverage lines with enterprise-grade features including authentication, authorization, caching, rate limiting, and comprehensive monitoring.

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend (React)                         │
│  - React 18 with Hooks                                      │
│  - React Router for navigation                              │
│  - React Query for server state management                  │
│  - React Hook Form for form handling                        │
│  - Axios for HTTP requests                                  │
└────────────────────┬────────────────────────────────────────┘
                     │ REST API (JSON)
┌────────────────────▼────────────────────────────────────────┐
│              Backend (Spring Boot 4.0.5)                    │
│  - Spring Security with JWT authentication                  │
│  - Spring Data JPA with Hibernate ORM                       │
│  - Spring Cache with Caffeine                               │
│  - Spring Actuator for monitoring                           │
│  - Bucket4j for rate limiting                               │
└────────────────────┬────────────────────────────────────────┘
                     │ JDBC
┌────────────────────▼────────────────────────────────────────┐
│              MySQL Database                                 │
│  - Indexed queries for performance                          │
│  - Normalized schema design                                 │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Technologies Used

### Backend Stack
- **Java 21** - Latest LTS version with virtual threads support
- **Spring Boot 4.0.5** - Modern Spring framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - ORM and database abstraction
- **Hibernate 7.2.7** - Object-relational mapping
- **MySQL 8.0** - Relational database
- **JWT (JJWT 0.11.5)** - Stateless authentication
- **Lombok** - Reduce boilerplate code
- **ModelMapper 3.1.1** - DTO mapping
- **Caffeine Cache** - In-memory caching
- **Bucket4j 8.10.1** - Token bucket rate limiting
- **SpringDoc OpenAPI 2.8.6** - API documentation
- **Maven 3.9+** - Build automation

### Frontend Stack
- **React 18.3.1** - UI library with hooks
- **React Router 6.26** - Client-side routing
- **React Query 5.59** - Server state management
- **React Hook Form 7.53** - Form state management
- **Axios 1.7.7** - HTTP client
- **Vite 7.0** - Build tool and dev server
- **Jest 29.7** - Unit testing framework
- **ESLint 9.15** - Code linting

### DevOps & Tools
- **Docker** - Containerization
- **Nginx** - Reverse proxy and static file serving
- **H2 Database** - In-memory testing database
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework

## ✨ Best Practices Implemented

### Backend Best Practices

#### 1. **API Design & Versioning**
- RESTful API design with semantic versioning (`/api/v1/*`)
- Parameterized API versioning through environment variables
- Consistent response format with `ApiResponse<T>` wrapper
- Proper HTTP status codes (200, 201, 400, 403, 404, 500)
- Comprehensive error handling with error codes

#### 2. **Security**
- JWT-based stateless authentication
- Role-based access control (RBAC) with `@PreAuthorize` annotations
- Password validation with strong requirements (uppercase, lowercase, numbers, special chars)
- Secure password hashing with BCrypt
- CORS configuration for cross-origin requests
- Request ID tracking for audit trails

#### 3. **Performance & Scalability**
- **Pagination**: All list endpoints support pagination with configurable page size
- **Database Indexing**: Strategic indexes on frequently queried columns
  - Email (unique index on Account)
  - Admin flag (index on Account)
  - Account ID, Line ID, Start Date (indexes on Policy)
  - Policy ID, Claim Status, Claim Date (indexes on Claim)
- **N+1 Query Prevention**: Eager loading and proper JOIN strategies
- **Caching**: Caffeine cache with 10-minute TTL for frequently accessed data
- **Virtual Threads**: Java 21 virtual threads for improved concurrency
- **Connection Pooling**: HikariCP for efficient database connections

#### 4. **Code Quality**
- **Lombok Annotations**: Reduces boilerplate with `@Data`, `@Builder`, `@Getter`, `@Setter`
- **Unified Password Validation**: Single source of truth via `PasswordValidator` utility
- **Consistent Authorization**: `@PreAuthorize` annotations instead of inline checks
- **DTO Pattern**: Separation of entity and API models
- **Service Layer**: Business logic abstraction from controllers
- **Repository Pattern**: Data access abstraction

#### 5. **Logging & Monitoring**
- **Structured Logging**: Request ID tracking via MDC (Mapped Diagnostic Context)
- **Spring Actuator**: Health checks, metrics, and monitoring endpoints
- **Prometheus Metrics**: Built-in metrics export
- **Request Logging**: Configurable request/response logging
- **Correlation IDs**: Track requests across system

#### 6. **Testing**
- **Unit Tests**: 61 comprehensive test cases
- **Controller Tests**: Mocked service layer testing
- **Service Tests**: Business logic validation
- **Test Coverage**: All critical paths covered
- **Mockito**: Dependency mocking for isolated tests

#### 7. **Data Validation**
- **Bean Validation**: JSR-380 annotations (`@NotNull`, `@Email`, `@Pattern`, etc.)
- **Custom Validators**: Business rule validation
- **Input Sanitization**: Protection against injection attacks
- **Type Safety**: Strong typing throughout

### Frontend Best Practices

#### 1. **Component Architecture**
- **Functional Components**: React hooks-based architecture
- **Component Composition**: Reusable, single-responsibility components
- **Props Validation**: PropTypes for runtime type checking
- **Custom Hooks**: Encapsulated logic for reusability

#### 2. **State Management**
- **React Query**: Server state management with caching
- **React Hook Form**: Efficient form state handling
- **Local State**: Minimal component state with hooks
- **Separation of Concerns**: UI state vs. server state

#### 3. **API Integration**
- **Centralized API Client**: Single source for API configuration
- **Parameterized Endpoints**: Environment-based API versioning
- **Error Handling**: Consistent error handling across requests
- **Request Interceptors**: Automatic JWT token injection
- **Response Interceptors**: Unified response processing

#### 4. **Code Quality**
- **ESLint**: Code style enforcement
- **Unused Import Detection**: Clean imports
- **Consistent Formatting**: Automated code formatting
- **Module Organization**: Logical file structure

#### 5. **Performance**
- **Code Splitting**: Vite's automatic chunk splitting
- **Lazy Loading**: Route-based code splitting
- **Caching**: React Query's built-in caching
- **Optimized Builds**: Production-ready minification

#### 6. **Testing**
- **Jest**: Unit testing framework
- **React Testing Library**: Component testing
- **API Mocking**: Axios mock adapter for tests
- **Coverage Reports**: Comprehensive test coverage

#### 7. **Environment Management**
- **Environment Variables**: `.env` files for configuration
- **Development/Production Configs**: Separate configurations
- **API Version Parameterization**: Dynamic API versioning
- **Secure Defaults**: Production-ready defaults

## 📋 Project Structure

```
demo-insurance-api/
├── demo-api/                          # Backend (Spring Boot)
│   ├── src/main/java/com/api/demo/
│   │   ├── config/                   # Configuration classes
│   │   │   ├── AppConfig.java        # Spring configuration
│   │   │   ├── JwtGenerator.java     # JWT token generation
│   │   │   ├── JwtValidator.java     # JWT validation
│   │   │   ├── SecurityContext.java  # Security context
│   │   │   ├── RateLimitConfig.java  # Rate limiting
│   │   │   └── VirtualThreadConfig.java
│   │   ├── controller/               # REST endpoints
│   │   │   ├── AccountController.java
│   │   │   ├── PolicyController.java
│   │   │   ├── ClaimController.java
│   │   │   ├── LineController.java
│   │   │   └── PublicController.java
│   │   ├── service/                  # Business logic
│   │   │   ├── AccountService.java
│   │   │   ├── PolicyService.java
│   │   │   ├── ClaimService.java
│   │   │   ├── LineService.java
│   │   │   ├── AuthorizationService.java
│   │   │   └── impl/                 # Service implementations
│   │   ├── repository/               # Data access
│   │   │   ├── AccountRepository.java
│   │   │   ├── PolicyRepository.java
│   │   │   ├── ClaimRepository.java
│   │   │   └── LineRepository.java
│   │   ├── entity/                   # JPA entities
│   │   │   ├── Account.java
│   │   │   ├── Policy.java
│   │   │   ├── Claim.java
│   │   │   ├── Line.java
│   │   │   └── Address.java
│   │   ├── dto/                      # Data transfer objects
│   │   │   ├── AccountDTO.java
│   │   │   ├── PolicyDTO.java
│   │   │   ├── ClaimDTO.java
│   │   │   └── LineDTO.java
│   │   ├── model/                    # Response models
│   │   │   ├── ApiResponse.java      # Unified response wrapper
│   │   │   ├── PagedResponse.java    # Pagination wrapper
│   │   │   ├── LoginRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   └── ClaimStatus.java
│   │   ├── filter/                   # HTTP filters
│   │   │   ├── JwtValidator.java
│   │   │   ├── RequestIdFilter.java
│   │   │   └── RequestLoggingFilter.java
│   │   ├── exception/                # Exception handling
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ResourceNotFoundException.java
│   │   ├── utils/                    # Utility classes
│   │   │   ├── JwtUtil.java
│   │   │   ├── PasswordValidator.java
│   │   │   └── NumberGenerator.java
│   │   └── DemoApplication.java      # Main application class
│   ├── src/test/java/                # Unit tests
│   ├── src/main/resources/
│   │   └── application.properties    # Configuration
│   └── pom.xml                       # Maven configuration
│
└── demo-ui/                           # Frontend (React)
    ├── src/
    │   ├── components/               # React components
    │   ├── pages/                    # Page components
    │   ├── api/
    │   │   └── client.js             # API client
    │   ├── config/
    │   │   └── api.config.js         # API configuration
    │   ├── hooks/                    # Custom React hooks
    │   ├── utils/                    # Utility functions
    │   ├── __tests__/                # Unit tests
    │   └── App.jsx                   # Root component
    ├── public/                       # Static assets
    ├── .env.example                  # Environment template
    ├── .env.development              # Development config
    ├── .env.production               # Production config
    ├── vite.config.js                # Vite configuration
    ├── jest.config.js                # Jest configuration
    ├── eslint.config.js              # ESLint configuration
    ├── package.json                  # Dependencies
    └── Dockerfile                    # Docker configuration
```

## 🔐 Authentication & Authorization

### JWT Authentication Flow
1. User submits credentials to `/api/v1/auth/login`
2. Server validates credentials and generates JWT token
3. Token is returned in response header and body
4. Client includes token in `Authorization: Bearer <token>` header
5. Server validates token on each request
6. Token contains user information and expiration time

### Authorization Levels
- **Public**: `/api/v1/auth/login`, `/api/v1/auth/register`, `/welcome`
- **Authenticated**: All other endpoints require valid JWT
- **Admin**: Specific endpoints require admin role
- **Owner**: Users can access their own resources

## 📊 Database Schema

### Entities
- **Account**: User accounts with authentication credentials
- **Policy**: Insurance policies linked to accounts and lines
- **Claim**: Insurance claims linked to policies
- **Line**: Insurance product lines (Auto, Home, etc.)
- **Address**: Embedded address information

### Key Relationships
- Account → Policies (1:N)
- Policy → Line (N:1)
- Policy → Claim (1:N)
- Account → Address (1:1 embedded)

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Node.js 18+
- MySQL 8.0+
- Maven 3.9+
- npm or yarn

### Backend Setup

```bash
cd demo-api

# Configure database
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=insurance
export DB_USERNAME=root
export DB_PASSWORD=root

# Build
mvn clean install

# Run
mvn spring-boot:run

# Run tests
mvn test

# API Documentation
# Visit http://localhost:8080/swagger-ui.html
```

### Frontend Setup

```bash
cd demo-ui

# Install dependencies
npm install

# Development server
npm run dev

# Build for production
npm run build

# Run tests
npm test

# Lint code
npm run lint
npm run lint:fix
```

## 🔧 Configuration

### Backend Configuration (`application.properties`)

```properties
# API Versioning
api.version=v1

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/insurance
spring.datasource.username=root
spring.datasource.password=root

# Caching
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=10m

# Rate Limiting
rate.limit.enabled=false
rate.limit.capacity=100
rate.limit.refill.tokens=100
rate.limit.refill.duration=1m

# Request Logging
logging.request.enabled=false

# CORS
management.endpoints.web.cors.allowed-origins=http://localhost:5173
```

### Frontend Configuration (`.env`)

```env
VITE_API_VERSION=v1
VITE_API_BASE_URL=http://localhost:8080
```

## 📡 API Endpoints

### Authentication
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/register` - User registration
- `GET /welcome` - Health check

### Accounts
- `GET /api/v1/accounts` - List all accounts (paginated, admin only)
- `GET /api/v1/accounts/{id}` - Get account by ID
- `GET /api/v1/accounts/email/{email}` - Get account by email
- `POST /api/v1/accounts` - Create account
- `PUT /api/v1/accounts/{id}` - Update account
- `DELETE /api/v1/accounts/{id}` - Delete account

### Policies
- `GET /api/v1/policies` - List all policies
- `GET /api/v1/policies/{id}` - Get policy by ID
- `POST /api/v1/policies` - Create policy
- `PUT /api/v1/policies/{id}` - Update policy
- `DELETE /api/v1/policies/{id}` - Delete policy

### Claims
- `GET /api/v1/claims` - List all claims (paginated)
- `GET /api/v1/claims/{id}` - Get claim by ID
- `POST /api/v1/claims/{id}` - Create claim
- `PUT /api/v1/claims/{id}` - Update claim
- `PATCH /api/v1/claims/{id}` - Partial update claim
- `DELETE /api/v1/claims/{id}` - Delete claim

### Lines
- `GET /api/v1/lines` - List all lines
- `GET /api/v1/lines/{id}` - Get line by ID
- `POST /api/v1/lines` - Create line
- `PUT /api/v1/lines/{id}` - Update line
- `DELETE /api/v1/lines/{id}` - Delete line

## 📈 Performance Optimizations

### Database
- Strategic indexing on frequently queried columns
- Connection pooling with HikariCP
- Query optimization with eager loading
- Pagination for large datasets

### Caching
- Caffeine in-memory cache with 10-minute TTL
- Cache invalidation on data mutations
- Reduced database load

### API
- Gzip compression for responses
- Efficient JSON serialization
- Minimal payload sizes with DTOs

### Frontend
- Code splitting with Vite
- Lazy loading of routes
- React Query caching
- Optimized re-renders with hooks

## 🧪 Testing

### Backend Tests
```bash
cd demo-api
mvn test
```

### Frontend Tests
```bash
cd demo-ui
npm test
npm test -- --coverage
```

### Test Coverage
- **Backend**: 61 test cases covering controllers, services, and repositories
- **Frontend**: Unit tests for components and API client

## 🐳 Docker Deployment

### Build Images
```bash
# Backend
cd demo-api
docker build -t insurance-api:latest .

# Frontend
cd demo-ui
docker build -t insurance-ui:latest .
```

### Run Containers
```bash
docker run -p 8080:8080 insurance-api:latest
docker run -p 80:80 insurance-ui:latest
```

## 📚 API Documentation

Interactive API documentation is available at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔍 Monitoring & Observability

### Health Checks
- `GET /actuator/health` - Application health status

### Metrics
- `GET /actuator/metrics` - Available metrics
- `GET /actuator/prometheus` - Prometheus metrics

### Request Tracking
- All requests include unique request IDs
- Correlation IDs for distributed tracing
- Structured logging with MDC

## 🛡️ Security Considerations

1. **Authentication**: JWT tokens with expiration
2. **Authorization**: Role-based access control
3. **Password Security**: BCrypt hashing with strong requirements
4. **CORS**: Configurable cross-origin policies
5. **Input Validation**: Comprehensive bean validation
6. **Rate Limiting**: Token bucket algorithm
7. **Error Handling**: Generic error messages to prevent information leakage

## 📝 License

This project is provided as-is for educational and commercial use.

## 🤝 Contributing

Contributions are welcome! Please follow the established code style and best practices.

## 📞 Support

For issues, questions, or suggestions, please open an issue in the repository.

---

**Last Updated**: April 2026
**Version**: 1.0.0
