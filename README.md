# Insurance Management System

A modern, full-stack insurance management application demonstrating enterprise-grade architecture, security best practices, and cutting-edge technologies. Built with Spring Boot 4, React 18, and MySQL 8, featuring JWT authentication, role-based access control, and hot module replacement for optimal developer experience.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.3-blue.svg)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-7.0-646CFF.svg)](https://vitejs.dev/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/)

---

## 📚 Documentation

- **[Quick Start Guide](QUICK_START.md)** - Get up and running in 3 steps
- **[Development Guide](DEVELOPMENT.md)** - Detailed development workflow and troubleshooting
- **[Docker Profiles Guide](DOCKER_PROFILES.md)** - Understanding dev vs prod modes
- **[HMR Setup Guide](HMR_SETUP.md)** - Hot Module Replacement configuration
- **[API Documentation](http://localhost:8080/swagger-ui/index.html)** - Interactive API docs (when running)

---

## 🎯 Key Features

### 🔐 Authentication & Security
- **Dual Authentication System**: HTTP Basic Auth for login, JWT Bearer tokens for API access
- **Role-Based Access Control (RBAC)**: Granular permissions for Admin and User roles
- **Automatic Token Management**: Axios interceptors handle token attachment and 401 redirects
- **Secure Password Storage**: BCrypt hashing with complexity validation
- **Authorization at Service Layer**: Business logic enforces ownership and admin checks

### 👥 User Management
- **Self-Service Registration**: Account creation with comprehensive validation
- **Profile Management**: Users edit their profiles, admins manage all accounts
- **Admin Controls**: Full CRUD operations on accounts with safety checks
- **Account Ownership**: Users cannot delete their own accounts

### 📋 Policy Management
- **Policy Lifecycle**: Create, read, update policies linked to accounts and insurance lines
- **Inline Editing**: Edit policies directly in cards without page navigation
- **Authorization**: Admins manage all policies; users manage only their own
- **Deletion Control**: Only admins can delete policies

### 🏥 Claims Management
- **Claims Workflow**: Submit, track, and update claims with status progression
- **Status Management**: SUBMITTED → IN_PROGRESS → APPROVED/DENIED
- **Policy Association**: Claims tied to specific policies for ownership validation
- **Edit Capabilities**: Update claim details, descriptions, and status
- **Admin Oversight**: Admins can manage all claims; users manage their own

### 🏢 Insurance Lines
- **Product Catalog**: Define insurance products (Auto, Home, Life, Health)
- **Coverage Configuration**: Set minimum and maximum coverage amounts
- **Admin-Only Management**: Complete CRUD operations restricted to administrators

### 🎨 Modern UI/UX
- **Loading States**: Animated spinners during asynchronous operations
- **Inline Editing**: Edit records without page navigation
- **Responsive Design**: Grid layout adapts to all screen sizes
- **Error Handling**: User-friendly error messages with auto-dismiss
- **Optimistic Updates**: Immediate UI feedback with React Query
- **Hot Module Replacement**: Instant updates during development

---

## 🏗️ Technology Stack

### Backend Technologies

| Technology | Version | Purpose | Why We Chose It |
|------------|---------|---------|-----------------|
| **Java** | 21 | Programming Language | Latest LTS with Virtual Threads (Project Loom) |
| **Spring Boot** | 4.0 | Application Framework | Industry standard, comprehensive ecosystem |
| **Spring Security** | 6.x | Security Framework | Robust authentication/authorization, JWT support |
| **Spring Data JPA** | 3.x | Data Access | Simplified database operations, repository pattern |
| **Hibernate** | 6.x | ORM | Mature ORM with excellent Spring integration |
| **MySQL** | 8.0 | Database | Reliable, performant, widely supported |
| **ModelMapper** | 3.x | Object Mapping | Automatic entity-to-DTO conversion |
| **SpringDoc OpenAPI** | 2.x | API Documentation | Auto-generated Swagger UI documentation |
| **Spotless** | 2.x | Code Formatting | Consistent code style with Eclipse formatter |
| **JUnit 5** | 5.x | Testing Framework | Modern testing with comprehensive assertions |
| **Mockito** | 5.x | Mocking Framework | Unit testing with mocked dependencies |

### Frontend Technologies

| Technology | Version | Purpose | Why We Chose It |
|------------|---------|---------|-----------------|
| **React** | 18.3 | UI Library | Component-based, concurrent features, huge ecosystem |
| **Vite** | 7.0 | Build Tool | Lightning-fast HMR, optimized builds |
| **React Router** | 6.26 | Routing | Modern routing with data loading patterns |
| **React Query** | 5.59 | Data Fetching | Automatic caching, background updates, loading states |
| **Axios** | 1.7 | HTTP Client | Promise-based, interceptors for centralized logic |
| **Jest** | 29.7 | Testing Framework | Comprehensive testing with coverage reports |
| **React Testing Library** | 14.2 | Component Testing | User-centric testing approach |
| **ESLint** | 9.15 | Code Quality | Enforce code standards and catch errors |

### DevOps & Infrastructure

| Technology | Purpose | Benefits |
|------------|---------|----------|
| **Docker** | Containerization | Consistent environments, easy deployment |
| **Docker Compose** | Orchestration | Multi-container management with profiles |
| **nginx** | Web Server | Production-grade serving of React app |
| **Maven** | Build Tool | Dependency management, build lifecycle |
| **Git Hooks** | Pre-commit Checks | Automated formatting and testing |

---

## 🎓 Best Practices Implemented

### Architecture & Design

✅ **Separation of Concerns**
- Clear separation between controller, service, and repository layers
- DTOs for API contracts, entities for database persistence
- Service layer contains all business logic and authorization

✅ **RESTful API Design**
- Standard HTTP methods (GET, POST, PUT, DELETE)
- Resource-based URLs (`/api/policies/{id}`)
- Proper status codes (200, 201, 400, 401, 404)

✅ **Security First**
- Authentication required for all protected endpoints
- Authorization checks at service layer
- Password complexity requirements
- JWT tokens with expiration

✅ **Clean Code**
- Consistent naming conventions
- Comprehensive comments and documentation
- Single Responsibility Principle
- DRY (Don't Repeat Yourself)

### Frontend Best Practices

✅ **Component Architecture**
- Reusable components (Spinner)
- Context for global state (Auth)
- Custom hooks for logic reuse
- Proper prop validation

✅ **Performance Optimization**
- React Query for caching and background updates
- Code splitting with React Router
- Optimized builds with Vite
- Lazy loading where appropriate

✅ **User Experience**
- Loading states for all async operations
- Error handling with user-friendly messages
- Inline editing for better workflow
- Responsive design for all devices

✅ **Code Quality**
- ESLint for code standards
- Comprehensive test coverage
- Consistent formatting
- Git hooks for quality gates

### Backend Best Practices

✅ **Data Access**
- Repository pattern with Spring Data JPA
- Entity relationships properly mapped
- Efficient queries with JPA
- Transaction management

✅ **Error Handling**
- Global exception handler
- Custom exceptions for business logic
- Consistent error responses
- Proper HTTP status codes

✅ **Testing**
- Unit tests for services
- Controller tests
- Mockito for dependencies
- High test coverage

✅ **Code Quality**
- Spotless for formatting
- Maven for build management
- Comprehensive logging
- Git hooks for quality gates

---

## 🚀 Quick Start

### Prerequisites

- **Docker Desktop** (recommended) - [Download](https://www.docker.com/products/docker-desktop/)
- **OR** Local setup:
  - Java 21+ - [Download](https://adoptium.net/)
  - Maven 3.9+ - [Download](https://maven.apache.org/download.cgi)
  - MySQL 8.0+ - [Download](https://dev.mysql.com/downloads/mysql/)
  - Node.js 20+ - [Download](https://nodejs.org/)

### Option 1: Docker (Recommended)

**Development Mode** (with Hot Module Replacement):

```bash
# Windows
start-dev.bat

# Linux/Mac
chmod +x start-dev.sh
./start-dev.sh

# Or manually
docker-compose --profile dev up --build
```

**Access the application:**
- Frontend (Dev): http://localhost:5173
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Database: localhost:3306

**Production Mode** (nginx serving built files):

```bash
# Windows
start-prod.bat

# Linux/Mac
chmod +x start-prod.sh
./start-prod.sh

# Or manually
docker-compose --profile prod up --build
```

**Access the application:**
- Frontend (Prod): http://localhost:3000
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- Database: localhost:3306

### Option 2: Local Development

#### Backend Setup

```bash
# 1. Create MySQL database
mysql -u root -p -e "CREATE DATABASE insurance;"

# 2. (Optional) Load seed data
mysql -u root -p insurance < docker/init.sql

# 3. Navigate to backend directory
cd demo-api

# 4. Run the application
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

**Override database credentials if needed:**
```bash
DB_HOST=localhost DB_PORT=3306 DB_NAME=insurance DB_USERNAME=root DB_PASSWORD=yourpassword ./mvnw spring-boot:run
```

**Backend runs on:** http://localhost:8080

#### Frontend Setup

```bash
# 1. Navigate to frontend directory
cd demo-ui

# 2. Install dependencies
npm install

# 3. Start development server
npm run dev
```

**Frontend runs on:** http://localhost:5173

The Vite dev server automatically proxies API calls to `localhost:8080`.

---

## 🔑 Test Credentials

The database is seeded with test accounts (all passwords: `Password1!`):

| Email | Password | Role | Description |
|-------|----------|------|-------------|
| test@test.com | Password1! | **Admin** | Full access to all accounts, policies, claims, and lines |
| john.doe@example.com | Password1! | User | Regular user with 3 policies and claims |
| jane.smith@example.com | Password1! | User | Regular user with 2 policies and claims |
| bob.johnson@example.com | Password1! | User | Regular user with 3 policies and claims |

---

## 📁 Project Structure

```
insurance-management-system/
├── demo-api/                           # Spring Boot Backend
│   ├── src/main/java/com/api/demo/
│   │   ├── config/                     # Configuration classes
│   │   │   ├── AppConfig.java          # ModelMapper, CORS
│   │   │   ├── JwtGenerator.java       # JWT token generation
│   │   │   ├── JwtValidator.java       # JWT token validation
│   │   │   ├── SecurityContext.java    # Spring Security config
│   │   │   └── VirtualThreadConfig.java # Virtual threads setup
│   │   ├── constraints/                # Validation constraints
│   │   │   └── SizeConstraints.java    # Field size constants
│   │   ├── controller/                 # REST Controllers
│   │   │   ├── AccountController.java  # Account endpoints
│   │   │   ├── ClaimController.java    # Claim endpoints
│   │   │   ├── CoverageController.java # Coverage calculation
│   │   │   ├── LineController.java     # Insurance line endpoints
│   │   │   ├── PolicyController.java   # Policy endpoints
│   │   │   └── PublicController.java   # Public endpoints (login, register)
│   │   ├── dto/                        # Data Transfer Objects
│   │   │   ├── AccountDTO.java         # Account response
│   │   │   ├── ClaimDTO.java           # Claim response
│   │   │   ├── LineDTO.java            # Line response
│   │   │   └── PolicyDTO.java          # Policy response
│   │   ├── entity/                     # JPA Entities
│   │   │   ├── Account.java            # User account
│   │   │   ├── Address.java            # Embedded address
│   │   │   ├── Claim.java              # Insurance claim
│   │   │   ├── Line.java               # Insurance product line
│   │   │   └── Policy.java             # Insurance policy
│   │   ├── exception/                  # Exception handling
│   │   │   ├── GlobelExceptionHandler.java # Global error handler
│   │   │   └── ResourceNotFoundException.java # 404 exception
│   │   ├── model/                      # Enums and models
│   │   │   ├── ClaimStatus.java        # Claim status enum
│   │   │   └── Response.java           # Generic response wrapper
│   │   ├── repository/                 # Spring Data Repositories
│   │   │   ├── AccountRepository.java  # Account data access
│   │   │   ├── ClaimRepository.java    # Claim data access
│   │   │   ├── LineRepository.java     # Line data access
│   │   │   └── PolicyRepository.java   # Policy data access
│   │   ├── service/                    # Service interfaces
│   │   │   ├── AccountService.java
│   │   │   ├── AccountUserDetailsService.java # Spring Security integration
│   │   │   ├── AuthorizationService.java # Authorization logic
│   │   │   ├── ClaimService.java
│   │   │   ├── CoverageCalculationService.java
│   │   │   ├── LineService.java
│   │   │   ├── PolicyService.java
│   │   │   └── impl/                   # Service implementations
│   │   │       ├── AccountServiceImpl.java
│   │   │       ├── ClaimServiceImpl.java
│   │   │       ├── LineServiceImpl.java
│   │   │       └── PolicyServiceImpl.java
│   │   └── DemoApplication.java        # Spring Boot main class
│   ├── src/main/resources/
│   │   └── application.properties      # Application configuration
│   ├── src/test/                       # Test suite
│   │   ├── controller/                 # Controller tests
│   │   └── service/                    # Service tests
│   ├── pom.xml                         # Maven dependencies
│   ├── mvnw                            # Maven wrapper (Unix)
│   └── mvnw.cmd                        # Maven wrapper (Windows)
│
├── demo-ui/                            # React Frontend
│   ├── src/
│   │   ├── api/
│   │   │   └── client.js               # Axios HTTP client with interceptors
│   │   ├── components/
│   │   │   └── Spinner.jsx             # Loading spinner component
│   │   ├── context/
│   │   │   └── AuthContext.jsx         # Authentication context
│   │   ├── pages/                      # Page components
│   │   │   ├── Accounts.jsx            # Account management (React Query)
│   │   │   ├── Claims.jsx              # Claims management
│   │   │   ├── CoverageCalculator.jsx  # Coverage calculator
│   │   │   ├── Home.jsx                # Landing page
│   │   │   ├── Lines.jsx               # Insurance lines
│   │   │   ├── Login.jsx               # Login page
│   │   │   ├── Policies.jsx            # Policy management
│   │   │   └── Register.jsx            # Registration page
│   │   ├── __tests__/                  # Jest tests
│   │   │   ├── context/                # Context tests
│   │   │   └── pages/                  # Page tests
│   │   ├── App.jsx                     # Main app component
│   │   ├── index.css                   # Global styles
│   │   └── main.jsx                    # React entry point
│   ├── Dockerfile                      # Production build
│   ├── nginx.conf                      # nginx configuration
│   ├── vite.config.js                  # Vite configuration
│   ├── jest.config.js                  # Jest configuration
│   ├── eslint.config.js                # ESLint configuration
│   ├── package.json                    # npm dependencies
│   └── .env.example                    # Environment variables template
│
├── docker/
│   └── init.sql                        # Database schema and seed data
│
├── Dockerfile                          # Backend Docker build
├── docker-compose.yml                  # Multi-container orchestration
├── start-dev.bat                       # Windows dev mode script
├── start-dev.sh                        # Unix dev mode script
├── start-prod.bat                      # Windows prod mode script
├── start-prod.sh                       # Unix prod mode script
│
├── QUICK_START.md                      # Quick start guide
├── DEVELOPMENT.md                      # Development guide
├── DOCKER_PROFILES.md                  # Docker profiles guide
├── HMR_SETUP.md                        # HMR configuration guide
└── README.md                           # This file
```

---

## 🔐 Authentication Flow

### 1. Register New Account

```http
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

**Password Requirements:**
- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one number
- At least one special character

### 2. Login (Get JWT Token)

```http
GET /login
Authorization: Basic <base64(email:password)>
```

**Response:**
```http
HTTP/1.1 200 OK
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

The JWT token is returned in the `Authorization` response header.

### 3. Use JWT on Protected Endpoints

```http
GET /api/accounts/
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Token Management:**
- Frontend stores JWT in localStorage
- Axios interceptor automatically attaches token to requests
- 401 responses trigger automatic logout and redirect

---

## 📡 API Endpoints

### Public Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/register` | Create new account | None |
| GET | `/login` | Sign in, receive JWT | Basic Auth |
| GET | `/welcome` | Health check | None |

### Account Management

| Method | Endpoint | Description | Auth | Admin Only |
|--------|----------|-------------|------|------------|
| GET | `/api/accounts/` | List all accounts | JWT | ✅ |
| GET | `/api/accounts/{id}` | Get account by ID | JWT | Own or Admin |
| GET | `/api/accounts/email` | Get by email | JWT | Own or Admin |
| PUT | `/api/accounts/{id}` | Update account | JWT | Own or Admin |
| DELETE | `/api/accounts/{id}` | Delete account | JWT | ✅ (not self) |

### Policy Management

| Method | Endpoint | Description | Auth | Admin Only |
|--------|----------|-------------|------|------------|
| GET | `/api/policies/` | List policies | JWT | All or Own |
| GET | `/api/policies/{id}` | Get policy by ID | JWT | Own or Admin |
| POST | `/api/policies/{accountId}` | Create policy | JWT | Own or Admin |
| PUT | `/api/policies/{id}` | Update policy | JWT | Own or Admin |
| DELETE | `/api/policies/{id}` | Delete policy | JWT | ✅ |

### Claims Management

| Method | Endpoint | Description | Auth | Admin Only |
|--------|----------|-------------|------|------------|
| GET | `/api/claims/` | List claims | JWT | All or Own |
| GET | `/api/claims/{id}` | Get claim by ID | JWT | Own or Admin |
| POST | `/api/claims/{policyId}` | Create claim | JWT | Own or Admin |
| PUT | `/api/claims/{id}` | Update claim | JWT | Own or Admin |
| DELETE | `/api/claims/{id}` | Delete claim | JWT | ✅ |

**Claim Statuses:** `SUBMITTED`, `IN_PROGRESS`, `APPROVED`, `DENIED`

### Insurance Lines

| Method | Endpoint | Description | Auth | Admin Only |
|--------|----------|-------------|------|------------|
| GET | `/api/lines/` | List all lines | JWT | No |
| GET | `/api/lines/{id}` | Get line by ID | JWT | No |
| POST | `/api/lines/` | Create line | JWT | ✅ |
| PUT | `/api/lines/{id}` | Update line | JWT | ✅ |
| DELETE | `/api/lines/{id}` | Delete line | JWT | ✅ |

### Coverage Calculator

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/coverage/calculate/{accountId}/{lineId}` | Calculate coverage | JWT |

---

## 🧪 Testing

### Backend Tests

```bash
cd demo-api

# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report

# Run specific test class
./mvnw test -Dtest=AccountServiceImplTest

# Skip tests during build
./mvnw package -DskipTests
```

**Test Coverage:**
- Service layer unit tests with Mockito
- Controller unit tests
- Authorization service tests
- Repository integration tests (H2 in-memory database)

### Frontend Tests

```bash
cd demo-ui

# Run all tests
npm test

# Run with coverage
npm test -- --coverage

# Run in watch mode
npm run test:watch

# Run specific test file
npm test -- Login.test.jsx
```

**Test Coverage:**
- Component rendering tests
- User interaction tests
- Context provider tests
- API client tests
- Coverage thresholds: 85% lines, 80% branches

---

## 🎨 Code Quality

### Backend Formatting

```bash
cd demo-api

# Format code
./mvnw spotless:apply

# Check formatting
./mvnw spotless:check
```

**Configuration:**
- Eclipse formatter
- Consistent indentation (tabs)
- Import organization
- Line length: 120 characters

### Frontend Linting

```bash
cd demo-ui

# Lint code
npm run lint

# Auto-fix issues
npm run lint:fix
```

**Configuration:**
- ESLint with React plugins
- React Hooks rules
- React Refresh rules
- No unused variables

### Pre-Commit Hooks

Located at `.git/hooks/pre-commit`:

**Automatic Checks:**
1. Format Java code with Spotless
2. Lint JavaScript/React code
3. Run backend tests
4. Run frontend tests
5. Re-stage formatted files
6. **Block commit if tests fail**

**Setup:**
```bash
# Make hook executable (Unix/Mac)
chmod +x .git/hooks/pre-commit
```

---

## ⚙️ Configuration

### Backend Configuration

File: `demo-api/src/main/resources/application.properties`

| Property | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | `localhost` | MySQL host |
| `DB_PORT` | `3306` | MySQL port |
| `DB_NAME` | `insurance` | Database name |
| `DB_USERNAME` | `root` | Database username |
| `DB_PASSWORD` | `root` | Database password |
| `spring.threads.virtual.enabled` | `true` | Enable Java 21 virtual threads |
| `spring.jpa.hibernate.ddl-auto` | `update` | Schema update strategy |
| `spring.jpa.show-sql` | `false` | Show SQL queries in logs |

**Environment Variables:**
```bash
# Override in shell
export DB_HOST=myhost
export DB_PORT=3307
export DB_NAME=mydb
export DB_USERNAME=myuser
export DB_PASSWORD=mypassword

# Or inline
DB_HOST=myhost ./mvnw spring-boot:run
```

### Frontend Configuration

File: `demo-ui/.env.local` (create from `.env.example`)

```env
# API URL for proxying requests
VITE_API_URL=http://localhost:8080
```

**Vite automatically loads:**
- `.env` - All environments
- `.env.local` - Local overrides (gitignored)
- `.env.development` - Development mode
- `.env.production` - Production build

---

## 🚀 Deployment

### Production Build

**Backend:**
```bash
cd demo-api
./mvnw clean package -DskipTests
# JAR file: target/demo-0.0.1-SNAPSHOT.jar

# Run JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

**Frontend:**
```bash
cd demo-ui
npm run build
# Build output: dist/

# Preview build
npm run preview
```

### Docker Production

```bash
# Build and start production mode
docker-compose --profile prod up --build -d

# Access at http://localhost:3000
```

**Production Stack:**
- MySQL 8.0 (persistent volume)
- Spring Boot (JAR execution)
- nginx (serving React build)

---

## 🔧 Troubleshooting

### Common Issues

**Port Already in Use:**
```bash
# Check what's using the port
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Linux/Mac

# Kill the process or change port in application.properties
```

**Database Connection Failed:**
```bash
# Verify MySQL is running
docker-compose ps

# Check logs
docker-compose logs mysql

# Reset database
docker-compose down -v
docker-compose up --build
```

**HMR Not Working:**
```bash
# Restart dev container
docker-compose restart ui-dev

# Check logs
docker-compose logs -f ui-dev

# Verify WebSocket connection in browser console
```

**Tests Failing:**
```bash
# Backend - clean and rebuild
cd demo-api
./mvnw clean test

# Frontend - clear cache
cd demo-ui
rm -rf node_modules
npm install
npm test
```

---

## 📊 Performance

### Java 21 Virtual Threads

The application uses **Project Loom** virtual threads for improved concurrency:

```java
@Configuration
public class VirtualThreadConfig {
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }
}
```

**Benefits:**
- Handle thousands of concurrent requests
- No thread pool tuning needed
- Better resource utilization
- Ideal for blocking I/O (JPA/MySQL)

### React Query Caching

The Accounts page uses React Query for optimized data fetching:

```javascript
const { data: accounts, isLoading } = useQuery({
  queryKey: ['accounts'],
  queryFn: async () => (await getAccounts()).data,
})
```

**Benefits:**
- Automatic caching
- Background refetching
- Optimistic updates
- Loading states
- Error handling

---

## 🤝 Contributing

### Development Workflow

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make changes** and test thoroughly
4. **Format code**: `./mvnw spotless:apply` and `npm run lint:fix`
5. **Run tests**: `./mvnw test` and `npm test`
6. **Commit changes**: `git commit -m 'Add amazing feature'`
7. **Push to branch**: `git push origin feature/amazing-feature`
8. **Open a Pull Request**

### Code Standards

- Follow existing code style
- Write comprehensive tests
- Update documentation
- Use meaningful commit messages
- Keep PRs focused and small

---

## 📄 License

This project is a demonstration application for educational purposes.

---

## 🙏 Acknowledgments

- **Spring Boot Team** - Excellent framework and documentation
- **React Team** - Modern UI library with great DX
- **Vite Team** - Lightning-fast build tool
- **TanStack Query** - Powerful data fetching library
- **Docker** - Simplified deployment and development

---

## 📞 Support

For questions, issues, or contributions:

1. Check the [documentation](#-documentation)
2. Review [troubleshooting](#-troubleshooting)
3. Open an issue on GitHub
4. Consult the [API documentation](http://localhost:8080/swagger-ui/index.html)

---

**Built with ❤️ using modern technologies and best practices**
