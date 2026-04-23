# Demo Insurance Management App

A modern, full-stack insurance management application demonstrating enterprise-grade architecture, best practices, and cutting-edge technologies. Built with Spring Boot 4.0 and React 18, this system showcases professional development patterns for scalable, maintainable applications.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.3-blue.svg)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-7.0-646CFF.svg)](https://vitejs.dev/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/)

<a name="top"></a>

---

## 📖 Table of Contents

- [🏗️ Architecture Overview](#-architecture-overview)
- [🛠️ Technology Stack](#-technology-stack)
- [🚀 Quick Start](#-quick-start)
- [✨ Best Practices Implemented](#-best-practices-implemented)
- [📋 Project Structure](#-project-structure)
- [📚 API Endpoints](#-api-endpoints)
- [🔐 Authentication & Authorization](#-authentication--authorization)
- [🔄 Circuit Breaker Pattern](#-circuit-breaker-pattern)
- [🗄️ Database Schema](#-database-schema)
- [🧪 Testing](#-testing)
- [📊 Performance Optimizations](#-performance-optimizations)
- [🔍 Monitoring & Observability](#-monitoring--observability)
- [📝 API Response Format](#-api-response-format)
- [🛡️ Security Considerations](#-security-considerations)
- [📦 Deployment](#-deployment)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [📞 Support](#-support)
- [🎯 Key Features](#-key-features)

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend (React 18)                      │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Pages │ Components │ Hooks │ Context │ API Client   │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕ (Axios)
┌─────────────────────────────────────────────────────────────┐
│                  Backend (Spring Boot 4.0)                  │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Controllers │ Services │ Repositories │ Entities     │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Security │ Validation │ Caching │ Rate Limiting      │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕ (JPA)
┌─────────────────────────────────────────────────────────────┐
│                    MySQL Database                           │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Accounts │ Policies │ Claims │ Lines │ Addresses     │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

[⬆ Back to Top](#top)

## 🛠️ Technology Stack

### Backend
| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 21 | Language |
| **Spring Boot** | 4.0.5 | Framework |
| **Spring Security** | 4.0.5 | Authentication & Authorization |
| **Spring Data JPA** | 4.0.5 | ORM & Database Access |
| **Spring Validation** | 4.0.5 | Input Validation |
| **Spring Cache** | 4.0.5 | Caching Layer |
| **Spring Actuator** | 4.0.5 | Monitoring & Metrics |
| **MySQL** | 8.0+ | Database |
| **JWT (JJWT)** | 0.11.5 | Token-based Authentication |
| **ModelMapper** | 3.1.1 | DTO Mapping |
| **Bucket4j** | 8.10.1 | Rate Limiting |
| **Caffeine** | Latest | In-memory Cache |
| **Resilience4j** | 2.1.0 | Circuit Breaker Pattern |
| **SpringDoc OpenAPI** | 2.8.6 | API Documentation |
| **Lombok** | Latest | Boilerplate Reduction |
| **Maven** | 3.8+ | Build Tool |

### Frontend
| Technology | Version | Purpose |
|-----------|---------|---------|
| **React** | 18.3.1 | UI Framework |
| **React Router** | 6.26.0 | Client-side Routing |
| **Axios** | 1.7.7 | HTTP Client |
| **TanStack Query** | 5.59.0 | Server State Management |
| **React Hook Form** | 7.53.0 | Form Management |
| **Vite** | 7.0.0 | Build Tool |
| **ESLint** | 9.15.0 | Code Linting |
| **Jest** | 29.7.0 | Testing Framework |
| **Node.js** | 18+ | Runtime |

---

[⬆ Back to Top](#top)

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose installed

### All-in-One Setup (Recommended)
```bash
./start-dev.sh
```

This script will:
- Start MySQL database at `jdbc:mysql://localhost:3306/insurance_db`
- Start backend API at `http://localhost:8080`
- Start frontend development server at `http://localhost:5173`
- Automatically open application in browser

**Access Points:**
- 🌐 Frontend Application: `http://localhost:5173`
- 🔌 Backend API: `http://localhost:8080`
- 📚 API Documentation (Swagger): `http://localhost:8080/swagger-ui.html`
- 🏥 API Health Check: `http://localhost:8080/actuator/health`
- 📊 API Metrics: `http://localhost:8080/actuator/metrics`
- 🗄️ Database: `localhost:3306` (MySQL)

**Test Admin Credentials:**
- Username: test@test.com
- Password: Password1!

### Running Backend Only

**Prerequisites:**
- Java 21+
- Maven 3.8+
- MySQL 8.0+ running on `localhost:3306`

**Steps:**
```bash
cd demo-api

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

**Backend Access:**
- API Base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Health Check: `http://localhost:8080/actuator/health`

### Running Frontend Only

**Prerequisites:**
- Node.js 18+
- npm or yarn
- Backend API running at `http://localhost:8080`

**Steps:**
```bash
cd demo-ui

# Install dependencies
npm install

# Start development server
npm run dev
```

**Frontend Access:**
- Application URL: `http://localhost:5173`
- Backend API: `http://localhost:8080` (configured in `.env.development`)

### Production Deployment
```bash
./start-prod.sh
```

This script will:
- Build optimized frontend bundle
- Build backend JAR
- Start both services with production configuration
- Access application at `http://localhost:3000`

---

[⬆ Back to Top](#top)

## ✨ Best Practices Implemented

### Backend Best Practices

#### 1. **RESTful API Design**
- Proper HTTP methods: GET, POST, PATCH, DELETE
- Consistent endpoint naming: `/api/v1/{resource}`
- Versioned API endpoints for backward compatibility
- Standardized response format with `ApiResponse<T>` wrapper
- Pagination support with `PagedResponse<T>`

#### 2. **Security**
- JWT-based authentication with HttpOnly cookies
- Role-based access control (RBAC) with `@PreAuthorize`
- Password validation with strength requirements
- Secure password hashing
- CORS configuration with allowed methods
- Authorization checks at service layer
- XSS protection via HttpOnly cookie flag
- CSRF protection via SameSite cookie attribute

#### 3. **Performance & Scalability**
- Database indexing on frequently queried fields
- Pagination for large datasets (default 20 items/page)
- Caching with Caffeine for frequently accessed data
- N+1 query prevention with proper JPA relationships
- Read-only transactions for query operations (`@Transactional(readOnly=true)`)
- Virtual thread support (Java 21)
- Circuit Breaker pattern for fault tolerance
- Graceful degradation with fallback methods

#### 4. **Code Quality**
- Layered architecture (Controller → Service → Repository)
- Dependency injection with Spring
- DTO pattern for API contracts
- Entity mapping with ModelMapper
- Comprehensive input validation with Jakarta Validation
- Consistent error handling with custom exceptions
- Spotless code formatting

#### 5. **Logging & Monitoring**
- Request ID tracking for distributed tracing
- Structured logging with request context
- Spring Actuator endpoints for health checks
- Metrics collection for monitoring
- Circuit Breaker state monitoring via Actuator
- Circuit Breaker event tracking

#### 6. **Testing**
- Unit tests for all controllers
- Mockito for dependency mocking
- AssertJ for fluent assertions
- Test coverage for CRUD operations
- Error scenario testing

#### 7. **Data Validation**
- Bean validation annotations (`@NotNull`, `@Size`, etc.)
- Custom validation constraints
- Field-level and cross-field validation
- Validation error messages in API responses

### Frontend Best Practices

#### 1. **Component Architecture**
- Functional components with hooks
- Reusable component patterns
- Proper component composition
- Clear separation of concerns
- Props validation with PropTypes

#### 2. **State Management**
- React Context for authentication state
- TanStack Query for server state
- Local state with `useState` for UI state
- Custom hooks for logic reuse (`useEditedFields`)
- Proper state updates and cleanup

#### 3. **API Integration**
- Centralized API client with Axios
- Request/response interceptors
- Automatic cookie handling (HttpOnly)
- Response unwrapping for `ApiResponse<T>`
- Consistent error handling
- Parameterized API versioning
- No manual JWT token storage needed

#### 4. **Code Quality**
- ESLint configuration for code standards
- Consistent naming conventions
- Proper error handling with `getErrorMessage()` utility
- Form validation with React Hook Form
- Accessibility considerations

#### 5. **Performance**
- Code splitting with React Router
- Lazy loading of routes
- Efficient re-renders with proper dependencies
- Memoization where appropriate
- Optimized bundle size

#### 6. **Testing**
- Jest for unit testing
- React Testing Library for component testing
- Mock API responses
- User event simulation
- Coverage reporting

#### 7. **Environment Management**
- Environment-specific configurations
- `.env` files for secrets
- Parameterized API base URL and version
- Development vs. production builds

---

[⬆ Back to Top](#top)

## 📋 Project Structure

### Backend
```
demo-api/
├── src/main/java/com/api/demo/
│   ├── config/              # Configuration classes
│   │   ├── AppConfig.java
│   │   ├── SecurityContext.java
│   │   ├── JwtGenerator.java
│   │   ├── JwtValidator.java
│   │   └── RateLimitConfig.java
│   ├── controller/          # REST Controllers
│   │   ├── AccountController.java
│   │   ├── PolicyController.java
│   │   ├── ClaimController.java
│   │   ├── LineController.java
│   │   ├── CoverageController.java
│   │   └── PublicController.java
│   ├── service/             # Business Logic
│   │   ├── AccountService.java
│   │   ├── PolicyService.java
│   │   ├── ClaimService.java
│   │   ├── LineService.java
│   │   └── AuthorizationService.java
│   ├── repository/          # Data Access
│   │   ├── AccountRepository.java
│   │   ├── PolicyRepository.java
│   │   ├── ClaimRepository.java
│   │   └── LineRepository.java
│   ├── entity/              # JPA Entities
│   │   ├── Account.java
│   │   ├── Policy.java
│   │   ├── Claim.java
│   │   ├── Line.java
│   │   └── Address.java
│   ├── dto/                 # Data Transfer Objects
│   │   ├── AccountDTO.java
│   │   ├── PolicyDTO.java
│   │   ├── ClaimDTO.java
│   │   └── LineDTO.java
│   ├── model/               # Response Models
│   │   ├── ApiResponse.java
│   │   ├── PagedResponse.java
│   │   ├── ClaimStatus.java
│   │   └── LoginRequest.java
│   ├── filter/              # HTTP Filters
│   │   ├── RequestIdFilter.java
│   │   ├── RequestLoggingFilter.java
│   │   └── RateLimitFilter.java
│   ├── exception/           # Custom Exceptions
│   │   ├── GlobalExceptionHandler.java
│   │   └── ResourceNotFoundException.java
│   └── utils/               # Utility Classes
│       ├── JwtUtil.java
│       ├── PasswordValidator.java
│       └── NumberGenerator.java
├── src/test/java/           # Unit Tests
├── pom.xml                  # Maven Configuration
└── application.yaml         # Application Configuration (YAML)
```

### Frontend
```
demo-ui/
├── src/
│   ├── pages/               # Page Components
│   │   ├── Login.jsx
│   │   ├── Register.jsx
│   │   ├── Accounts.jsx
│   │   ├── Policies.jsx
│   │   ├── Claims.jsx
│   │   ├── Lines.jsx
│   │   ├── CoverageCalculator.jsx
│   │   └── Home.jsx
│   ├── components/          # Reusable Components
│   │   ├── Spinner.jsx
│   │   ├── ErrorBoundary.jsx
│   │   └── Navigation.jsx
│   ├── context/             # React Context
│   │   └── AuthContext.jsx
│   ├── hooks/               # Custom Hooks
│   │   └── useEditedFields.js
│   ├── api/                 # API Integration
│   │   └── client.js
│   ├── utils/               # Utility Functions
│   │   ├── errorHandler.js
│   │   └── secureStorage.js
│   ├── config/              # Configuration
│   │   └── api.config.js
│   ├── __tests__/           # Test Files
│   │   ├── pages/
│   │   ├── api/
│   │   └── context/
│   ├── App.jsx
│   └── main.jsx
├── .env.example             # Environment Template
├── .env.development         # Development Config
├── .env.production          # Production Config
├── package.json
├── vite.config.js
├── jest.config.js
└── eslint.config.js
```

---

[⬆ Back to Top](#top)

## 📚 API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new account
- `POST /api/v1/auth/login` - Login and get JWT token

### Accounts
- `GET /api/v1/accounts` - List all accounts (paginated)
- `GET /api/v1/accounts/{id}` - Get account details
- `PATCH /api/v1/accounts/{id}` - Update account (partial)
- `DELETE /api/v1/accounts/{id}` - Delete account

### Policies
- `GET /api/v1/policies` - List all policies
- `GET /api/v1/policies/{id}` - Get policy details
- `POST /api/v1/policies/{accountId}` - Create policy
- `PATCH /api/v1/policies/{id}` - Update policy (partial)
- `DELETE /api/v1/policies/{id}` - Delete policy

### Claims
- `GET /api/v1/claims` - List all claims (paginated)
- `GET /api/v1/claims/{id}` - Get claim details
- `POST /api/v1/claims/{policyId}` - Create claim
- `PATCH /api/v1/claims/{id}` - Update claim (partial)
- `DELETE /api/v1/claims/{id}` - Delete claim

### Lines
- `GET /api/v1/lines` - List all lines
- `GET /api/v1/lines/{id}` - Get line details
- `POST /api/v1/lines` - Create line (admin only)
- `PATCH /api/v1/lines/{id}` - Update line (admin only)
- `DELETE /api/v1/lines/{id}` - Delete line (admin only)

### Coverage
- `GET /api/v1/coverage/calculate/{accountId}/{lineId}` - Calculate coverage

---

[⬆ Back to Top](#top)

## 🔐 Authentication & Authorization

### JWT Token Flow
1. User registers or logs in
2. Backend generates JWT token with user claims
3. Frontend stores token in secure storage
4. Token sent in `Authorization: Bearer <token>` header
5. Backend validates token on each request
6. Token expires after configured duration

### Role-Based Access Control
- **ADMIN**: Full access to all resources
- **USER**: Access to own resources only

### Authorization Annotations
```java
@PreAuthorize("hasRole('ADMIN')")                    // Admin only
@PreAuthorize("hasRole('ADMIN') or @authorizationService.isOwner(#id)")  // Admin or owner
```

### HttpOnly Cookie Authentication
- JWT tokens stored in HttpOnly cookies (not accessible to JavaScript)
- Automatic cookie transmission with requests via `withCredentials: true`
- SameSite=Strict for CSRF protection
- Secure flag ensures HTTPS-only transmission in production
- Logout endpoint clears the authentication cookie

---

[⬆ Back to Top](#top)

## 🔄 Circuit Breaker Pattern

### Overview
Resilience4j Circuit Breaker pattern implemented across all service layers for fault tolerance and graceful degradation.

### Circuit Breaker States
- **CLOSED**: Normal operation, requests pass through
- **OPEN**: Service failing, requests rejected immediately, fallback invoked
- **HALF_OPEN**: Testing recovery with limited requests

### Protected Services
- **policyService**: Policy CRUD operations
- **claimService**: Claim CRUD operations
- **accountService**: Account CRUD operations
- **lineService**: Line CRUD operations
- **coverageService**: Coverage calculations

### Configuration
```yaml
resilience4j:
  circuitbreaker:
    configs:
      default:
        slidingWindowSize: 10              # Calls to evaluate
        minimumNumberOfCalls: 5            # Min calls before evaluating
        failureRateThreshold: 50%          # Failure rate to open
        waitDurationInOpenState: 10s       # Time before retry
        slowCallDurationThreshold: 2s      # Duration considered slow
```

### Monitoring Circuit Breakers
```bash
# List all circuit breakers
curl http://localhost:8080/actuator/circuitbreakers

# Get circuit breaker events
curl http://localhost:8080/actuator/circuitbreaker-events/policyService

# View metrics
curl http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.calls
```

### Fallback Behavior
Each protected method has a fallback that throws a user-friendly exception:
```java
@CircuitBreaker(name = "policyService", fallbackMethod = "getPolicyFallback")
public PolicyDTO getById(Integer policyId) { ... }

public PolicyDTO getPolicyFallback(Integer policyId, Exception e) {
    throw new RuntimeException("Policy service is currently unavailable. Please try again later.", e);
}
```

---

[⬆ Back to Top](#top)

## 🗄️ Database Schema

### Entities
- **Account**: User accounts with authentication
- **Policy**: Insurance policies linked to accounts
- **Claim**: Insurance claims linked to policies
- **Line**: Insurance line types (Auto, Home, etc.)
- **Address**: Address information for accounts

### Key Relationships
- Account → Policies (1:N)
- Account → Claims (through Policy)
- Policy → Claims (1:N)
- Policy → Line (N:1)
- Account → Address (1:1)

### Indexes
- Account: email, admin status
- Policy: accountId, lineId
- Claim: policyId, claimStatus
- Line: name

---

[⬆ Back to Top](#top)

## 🧪 Testing

### Backend Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AccountControllerTest

# Run with coverage
mvn test jacoco:report
```

### Frontend Tests
```bash
# Run all tests
npm test

# Run with coverage
npm test -- --coverage

# Watch mode
npm run test:watch
```

---

[⬆ Back to Top](#top)

## 📊 Performance Optimizations

### Backend
- **Pagination**: Default 20 items per page
- **Caching**: Caffeine cache for accounts and policies
- **Indexing**: Database indexes on foreign keys
- **Read-only Transactions**: `@Transactional(readOnly=true)` for queries
- **Lazy Loading**: Proper JPA fetch strategies
- **Rate Limiting**: Bucket4j for API rate limiting

### Frontend
- **Code Splitting**: Route-based code splitting
- **Lazy Loading**: React.lazy for components
- **Memoization**: React.memo for expensive components
- **Query Caching**: TanStack Query caching
- **Bundle Optimization**: Vite tree-shaking

---

[⬆ Back to Top](#top)

## 🔍 Monitoring & Observability

### Health Checks
```
GET /actuator/health
```

### Metrics
```
GET /actuator/metrics
```

### Request Tracking
- Request ID generated for each request
- Included in response headers
- Useful for distributed tracing

---

[⬆ Back to Top](#top)

## 📝 API Response Format

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* resource data */ },
  "timestamp": "2024-04-20T10:30:00Z",
  "requestId": "req-12345"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Validation failed",
  "data": null,
  "timestamp": "2024-04-20T10:30:00Z",
  "requestId": "req-12345"
}
```

### Paginated Response
```json
{
  "success": true,
  "message": "Accounts retrieved",
  "data": {
    "content": [ /* items */ ],
    "page": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

---

[⬆ Back to Top](#top)

## 🛡️ Security Considerations

- **HTTPS**: Use HTTPS in production
- **CORS**: Configured for specific origins
- **CSRF**: Spring Security CSRF protection
- **SQL Injection**: JPA parameterized queries
- **XSS**: React automatic escaping
- **Password**: Minimum 8 characters, strength validation
- **Token Expiry**: Configurable JWT expiration
- **Rate Limiting**: Prevent brute force attacks

---

[⬆ Back to Top](#top)

## 📦 Deployment

### Docker
```bash
# Build backend image
docker build -t insurance-api:latest ./demo-api

# Build frontend image
docker build -t insurance-ui:latest ./demo-ui

# Run with docker-compose
docker-compose up
```

### Production Checklist
- [ ] Set environment variables
- [ ] Configure database backups
- [ ] Enable HTTPS/SSL
- [ ] Configure logging
- [ ] Set up monitoring
- [ ] Configure rate limiting
- [ ] Enable caching
- [ ] Set JWT expiration
- [ ] Configure CORS origins
- [ ] Enable security headers

---

[⬆ Back to Top](#top)

## 🤝 Contributing

1. Create feature branch: `git checkout -b feature/name`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/name`
4. Submit pull request

### Code Standards
- Follow existing code style
- Write unit tests for new features
- Update documentation
- Run linting: `npm run lint` (frontend), `mvn spotless:apply` (backend)

---

[⬆ Back to Top](#top)

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

---

[⬆ Back to Top](#top)

## 📞 Support

For issues, questions, or suggestions:
1. Check existing documentation
2. Review API documentation at `/swagger-ui.html`
3. Check test files for usage examples
4. Create an issue with detailed description

---

[⬆ Back to Top](#top)

## 🎯 Key Features

✅ **JWT Authentication** - Secure HttpOnly cookie-based auth
✅ **Role-Based Access Control** - Admin and user roles
✅ **Circuit Breaker Pattern** - Fault tolerance and graceful degradation
✅ **Pagination** - Efficient data retrieval
✅ **Caching** - Improved performance with Caffeine
✅ **Rate Limiting** - API protection with Bucket4j
✅ **Input Validation** - Data integrity with Jakarta Validation
✅ **Error Handling** - Consistent error responses
✅ **API Documentation** - Swagger/OpenAPI
✅ **Comprehensive Testing** - Unit and integration tests
✅ **Modern Stack** - Latest technologies (Spring Boot 4.0, React 18)
✅ **Best Practices** - Enterprise patterns and architecture
✅ **Scalable Architecture** - Ready for growth
✅ **Monitoring & Observability** - Spring Actuator endpoints
✅ **YAML Configuration** - Clean, hierarchical configuration

---

**Last Updated**: April 2026
**Version**: 1.0.0

---

## 📋 Recent Updates

### v1.0.0 - April 2026

#### Security Enhancements
- **HttpOnly Cookies**: JWT tokens now stored in HttpOnly cookies instead of localStorage
  - Prevents XSS attacks by making tokens inaccessible to JavaScript
  - SameSite=Strict attribute for CSRF protection
  - Secure flag ensures HTTPS-only transmission in production
  - See [JWT_HTTPONLY_MIGRATION.md](JWT_HTTPONLY_MIGRATION.md) for details

#### Resilience & Fault Tolerance
- **Circuit Breaker Pattern**: Implemented Resilience4j across all service layers
  - Automatic failure detection and graceful degradation
  - Five service-specific circuit breakers (policy, claim, account, line, coverage)
  - Fallback methods for user-friendly error messages
  - See [CIRCUIT_BREAKER_SETUP.md](CIRCUIT_BREAKER_SETUP.md) for details

#### Configuration Management
- **YAML Configuration**: Migrated from properties to YAML format
  - Cleaner, hierarchical structure
  - Better organization of nested properties
  - Easier maintenance and readability
  - File: `application.yaml`

#### Performance Improvements
- **Read-Only Transactions**: Added `@Transactional(readOnly=true)` to all GET endpoints
  - Optimizes database query execution
  - Improves connection pooling efficiency
  - Better resource utilization

#### Monitoring & Observability
- **Actuator Endpoints**: Exposed circuit breaker monitoring
  - `/actuator/circuitbreakers` - List all circuit breakers
  - `/actuator/circuitbreaker-events` - View circuit breaker events
  - `/actuator/health` - Health status including circuit breakers
  - `/actuator/metrics` - Resilience4j metrics

---

**Last Updated**: April 2026
**Version**: 1.0.0
