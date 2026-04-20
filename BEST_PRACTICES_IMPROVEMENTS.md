# Best Practices Improvements - Priority Action Plan

## 🔴 CRITICAL (Security & Stability)

### 1. Move JWT Secret Key to Environment Variables
**Current Issue:** Hardcoded JWT key in `SecurityContext.java`
```java
public static final String JWT_KEY = "thisshouldbestoredsomewheresecure";
```

**Impact:** Security vulnerability - secret exposed in source code

**Action:**
- Move to `application.properties`: `jwt.secret=${JWT_SECRET:default-dev-key}`
- Update `JwtGenerator.java` and `JwtValidator.java` to inject from config
- Document in deployment guide

---

### 2. Add Missing Exception Handlers
**Current Issue:** `GlobalExceptionHandler` missing handlers for `SecurityException`, `BadCredentialsException`

**Impact:** Unhandled exceptions return 500 errors instead of proper 4xx responses

**Action:**
- Add `@ExceptionHandler(SecurityException.class)` → 403 Forbidden
- Add `@ExceptionHandler(BadCredentialsException.class)` → 401 Unauthorized
- Add catch-all `@ExceptionHandler(Exception.class)` → 500 with logging

---

### 3. Implement Token Expiration Validation
**Current Issue:** JWT expiration set to 30 million ms (~347 days) with no validation

**Impact:** Tokens never expire, security risk

**Action:**
- Add `jwt.expiration=${JWT_EXPIRATION:3600000}` (1 hour default)
- Validate expiration in `JwtValidator.java`
- Add refresh token endpoint

---

### 4. Enable Rate Limiting by Default
**Current Issue:** Rate limiting disabled by default, requires manual configuration

**Impact:** API vulnerable to brute force attacks

**Action:**
- Change `@ConditionalOnProperty` to always enable
- Make it configurable but default to enabled
- Add rate limit headers to responses

---

## 🟠 HIGH (Performance & Scalability)

### 5. Add Pagination to List Endpoints
**Current Issue:** `getAccounts()`, `getPolicies()`, `getClaims()`, `getLines()` return all records

**Impact:** Performance degrades with large datasets, memory issues

**Action:**
```java
@GetMapping("/")
public ResponseEntity<Page<AccountDTO>> getAllAccounts(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {
    return new ResponseEntity<>(accountService.findAll(PageRequest.of(page, size)), HttpStatus.OK);
}
```

---

### 6. Implement Eager Loading for Related Entities
**Current Issue:** No `@EntityGraph` or `@Transactional` on service methods

**Impact:** N+1 query problem, slow performance

**Action:**
- Add `@EntityGraph` to repository methods
- Add `@Transactional(readOnly=true)` to read operations
- Use projection DTOs to fetch only needed fields

---

### 7. Add Database Indexes
**Current Issue:** No indexing strategy documented

**Impact:** Slow queries on large tables

**Action:**
- Add index on `Account.email` (unique)
- Add index on `Policy.accountId` (foreign key)
- Add index on `Claim.policyId` (foreign key)
- Document indexing strategy

---

## 🟡 MEDIUM (Code Quality & Maintainability)

### 8. Consolidate Password Validation
**Current Issue:** Password validation in both `PasswordValidator` and `AccountDTO`

**Impact:** Duplication, maintenance burden

**Action:**
- Keep validation in `PasswordValidator` only
- Remove from DTOs
- Use `@Valid` annotation on controller parameters

---

### 9. Use @PreAuthorize for Authorization
**Current Issue:** Authorization checks inline in controllers

**Impact:** Mixed concerns, hard to test, inconsistent

**Action:**
```java
@PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
@GetMapping("/{id}")
public ResponseEntity<AccountDTO> getAccountById(@PathVariable Integer id) {
    return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);
}
```

---

### 10. Standardize API Response Format
**Current Issue:** Inconsistent response formats (Map vs Response object)

**Impact:** Frontend error handling fragile, inconsistent

**Action:**
```java
public class ApiResponse<T> {
    private T data;
    private String message;
    private boolean success;
    private LocalDateTime timestamp;
    private String errorCode;
}
```

---

## 🔵 MEDIUM (Testing)

### 11. Increase Test Coverage
**Current Issue:** Coverage 45-60%, missing filter tests, integration tests

**Impact:** Bugs slip through, refactoring risky

**Action:**
- Add integration tests for controllers
- Add filter tests (JwtGenerator, JwtValidator)
- Add security configuration tests
- Target 80%+ coverage for critical paths

---

### 12. Add Frontend Integration Tests
**Current Issue:** Frontend tests mock axios, no real API testing

**Impact:** API contract breaks go undetected

**Action:**
- Add MSW (Mock Service Worker) for realistic API mocking
- Add integration tests for auth flow
- Add E2E tests with Cypress/Playwright

---

## 🟢 LOW (Nice to Have)

### 13. Add API Versioning
**Current Issue:** Inconsistent versioning (/api/v1/auth/login vs /api/accounts/)

**Impact:** Hard to maintain backward compatibility

**Action:**
- Standardize to `/api/v1/*` for all endpoints
- Document versioning strategy

---

### 14. Add Structured Logging
**Current Issue:** Basic logging, no correlation IDs

**Impact:** Hard to debug in production

**Action:**
- Add SLF4J with JSON format
- Add request ID tracking
- Add distributed tracing (Spring Cloud Sleuth)

---

### 15. Create Configuration Profiles
**Current Issue:** Single application.properties for all environments

**Impact:** Risk of deploying dev config to production

**Action:**
- Create `application-dev.properties`
- Create `application-prod.properties`
- Document environment setup

---

## Implementation Priority

### Phase 1 (Week 1) - Critical Security
1. Move JWT secret to environment variables
2. Add missing exception handlers
3. Implement token expiration validation
4. Enable rate limiting by default

### Phase 2 (Week 2) - Performance
5. Add pagination to list endpoints
6. Implement eager loading
7. Add database indexes

### Phase 3 (Week 3) - Code Quality
8. Consolidate password validation
9. Use @PreAuthorize for authorization
10. Standardize API response format

### Phase 4 (Week 4) - Testing & Monitoring
11. Increase test coverage
12. Add frontend integration tests
13. Add structured logging

### Phase 5 (Ongoing) - Nice to Have
14. Add API versioning
15. Create configuration profiles

---

## Quick Wins (Can be done immediately)

✅ Remove unused import in AppConfig
✅ Fix deprecated Bucket4j methods
✅ Add @Transactional(readOnly=true) to read operations
✅ Add security headers to responses
✅ Enable HTTPS in production

---

## Estimated Effort

| Priority | Items | Effort | Impact |
|----------|-------|--------|--------|
| Critical | 4 | 8 hours | High |
| High | 3 | 16 hours | High |
| Medium | 4 | 20 hours | Medium |
| Low | 4 | 12 hours | Low |
| **Total** | **15** | **56 hours** | **High** |

---

## Success Metrics

- [ ] Test coverage > 80%
- [ ] All security issues resolved
- [ ] API response time < 200ms (p95)
- [ ] Zero unhandled exceptions in logs
- [ ] All endpoints paginated
- [ ] JWT expiration enforced
- [ ] Rate limiting active
- [ ] All dependencies up to date
- [ ] Security scanning in CI/CD
- [ ] Documentation complete
