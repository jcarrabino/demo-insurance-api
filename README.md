# Insurance Management System

A modern, full-stack insurance management application demonstrating enterprise-grade architecture, best practices, and cutting-edge technologies. Built with Spring Boot 4.0 and React 18, this system showcases professional development patterns for scalable, maintainable applications.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend (React 18)                       │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Pages │ Components │ Hooks │ Context │ API Client  │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕ (Axios)
┌─────────────────────────────────────────────────────────────┐
│                  Backend (Spring Boot 4.0)                   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Controllers │ Services │ Repositories │ Entities    │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Security │ Validation │ Caching │ Rate Limiting     │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕ (JPA)
┌─────────────────────────────────────────────────────────────┐
│                    MySQL Database                            │
│  ┌──────────────────────────────────────────────────────┐   │
│  │ Accounts │ Policies │ Claims │ Lines │ Addresses    │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

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

## ✨ Best Practices Implemented

### Backend Best Practices

#### 1. **RESTful API Design**
- Proper HTTP methods: GET, POST, PATCH, DELETE
- Consistent endpoint naming: `/api/v1/{resource}`
- Versioned API endpoints for backward compatibility
- Standardized response format with `ApiResponse<T>` wrapper
- Pagination support with `PagedResponse<T>`

#### 2. **Security**
- JWT-based authentication with Bearer tokens
- Role-based access control (RBAC) with `@PreAuthorize`
- Password validation with strength requirements
- Secure password hashing
- CORS configuration with allowed methods
- Authorization checks at service layer

#### 3. **Performance & Scalability**
- Database indexing on frequently queried fields
- Pagination for large datasets (default 20 items/page)
- Caching with Caffeine for frequently accessed data
- N+1 query prevention with proper JPA relationships
- Read-only transactions for query operations
- Virtual thread support (Java 21)

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
- Automatic JWT token injection
- Response unwrapping for `ApiResponse<T>`
- Consistent error handling
- Parameterized API versioning

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
└── application.properties   # Application Configuration
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

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+
- npm or yarn

### Backend Setup

1. **Clone and navigate**
```bash
git clone <repository>
cd demo-api
```

2. **Configure database**
```bash
# Update application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/insurance_db
spring.datasource.username=root
spring.datasource.password=your_password
```

3. **Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

4. **Access API documentation**
```
http://localhost:8080/swagger-ui.html
```

### Frontend Setup

1. **Navigate and install**
```bash
cd demo-ui
npm install
```

2. **Configure environment**
```bash
cp .env.example .env.development
# Update VITE_API_BASE_URL if needed
```

3. **Start development server**
```bash
npm run dev
```

4. **Access application**
```
http://localhost:5173
```

---

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

---

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

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

---

## 📞 Support

For issues, questions, or suggestions:
1. Check existing documentation
2. Review API documentation at `/swagger-ui.html`
3. Check test files for usage examples
4. Create an issue with detailed description

---

## 🎯 Key Features

✅ **JWT Authentication** - Secure token-based auth
✅ **Role-Based Access Control** - Admin and user roles
✅ **Pagination** - Efficient data retrieval
✅ **Caching** - Improved performance
✅ **Rate Limiting** - API protection
✅ **Input Validation** - Data integrity
✅ **Error Handling** - Consistent error responses
✅ **API Documentation** - Swagger/OpenAPI
✅ **Comprehensive Testing** - Unit and integration tests
✅ **Modern Stack** - Latest technologies
✅ **Best Practices** - Enterprise patterns
✅ **Scalable Architecture** - Ready for growth

---

**Last Updated**: April 2026
**Version**: 1.0.0
