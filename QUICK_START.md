# Quick Start Guide

## 🚀 Get Started in 3 Steps

### 1. Start Development Environment

**Windows:**
```bash
start-dev.bat
```

**Linux/Mac:**
```bash
chmod +x start-dev.sh
./start-dev.sh
```

**Or manually:**
```bash
docker-compose --profile dev up --build
```

### 2. Open Your Browser

- **Frontend (Dev)**: http://localhost:5173
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html

### 3. Start Coding!

Edit any file in `demo-ui/src/**` and watch it update instantly in your browser! ⚡

---

## 🔑 Test Credentials

| Email | Password | Role |
|-------|----------|------|
| test@test.com | Password1! | Admin |
| john.doe@example.com | Password1! | User |

---

## 🔥 Test Hot Module Replacement

1. Open http://localhost:5173
2. Edit `demo-ui/src/pages/Home.jsx`
3. Change the hero title: `<h1>Protect What Matters Most</h1>`
4. Save the file
5. Watch the browser update automatically! (No refresh needed)

Look for the comment: `🔥 HMR Test: Change the hero title below...`

---

## 📁 Project Structure

```
demo-api/          # Spring Boot backend
  src/main/java/   # Java source code
  src/test/        # JUnit tests

demo-ui/           # React frontend
  src/
    pages/         # React pages (Home, Login, Policies, etc.)
    components/    # Reusable components (Spinner)
    context/       # Auth context
    api/           # Axios client
```

---

## 🛠️ Common Commands

### Development

```bash
# Start dev mode (hot reload)
start-dev.bat

# Start production mode
start-prod.bat

# Stop all services
docker-compose down

# View logs
docker-compose logs -f ui-dev
```

### Testing

```bash
# Frontend tests
cd demo-ui
npm test

# Backend tests
cd demo-api
./mvnw test
```

### Code Quality

```bash
# Lint frontend
cd demo-ui
npm run lint:fix

# Format backend
cd demo-api
./mvnw spotless:apply
```

---

## 🐛 Troubleshooting

### HMR Not Working?

1. Check console for WebSocket errors
2. Restart container: `docker-compose restart ui-dev`
3. Hard refresh browser: Ctrl+Shift+R

### Port Already in Use?

```bash
# Stop all containers
docker-compose down

# Check what's using the port
netstat -ano | findstr :5173  # Windows
lsof -i :5173                 # Linux/Mac
```

### Database Issues?

```bash
# Reset database with fresh data
docker-compose down -v
docker-compose --profile dev up --build
```

---

## 📚 More Information

- **Full Documentation**: [README.md](README.md)
- **Development Guide**: [DEVELOPMENT.md](DEVELOPMENT.md)
- **API Documentation**: http://localhost:8080/swagger-ui/index.html

---

## 🎯 What to Build

Try these features to learn the codebase:

1. **Add a new field** to the Account form
2. **Create a new page** for viewing policy history
3. **Add a filter** to the Claims page
4. **Customize the theme** in `demo-ui/src/index.css`
5. **Add validation** to form inputs

Happy coding! 🎉
