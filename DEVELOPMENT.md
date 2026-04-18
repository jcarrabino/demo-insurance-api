# Development Guide

## Hot Module Replacement (HMR)

The development environment uses Vite's HMR for instant updates when you modify files.

### How It Works

1. **Start Dev Mode**:
   ```bash
   # Windows
   start-dev.bat
   
   # Linux/Mac or Manual
   docker-compose --profile dev up --build
   ```

2. **Access the App**:
   - Open http://localhost:5173 in your browser
   - The Vite dev server is running with HMR enabled

3. **Make Changes**:
   - Edit any file in `demo-ui/src/**`
   - Changes appear in browser within 1-2 seconds
   - No manual refresh needed!

### What Gets Hot Reloaded

✅ **Instant Updates (HMR)**:
- React components (`.jsx` files)
- CSS files (`.css`)
- JavaScript modules (`.js`)
- Context providers
- API client code

⚠️ **Requires Refresh**:
- `index.html` changes
- `vite.config.js` changes
- Environment variable changes
- New dependencies added to `package.json`

### Configuration Details

**Vite Config** (`demo-ui/vite.config.js`):
```javascript
server: {
  host: '0.0.0.0',           // Accept connections from Docker
  port: 5173,                 // Dev server port
  strictPort: true,           // Fail if port unavailable
  watch: {
    usePolling: true,         // Required for Docker file watching
    interval: 100,            // Check for changes every 100ms
  },
  hmr: {
    host: 'localhost',        // HMR WebSocket host
    port: 5173,               // HMR WebSocket port
  }
}
```

**Docker Compose** (`docker-compose.yml`):
```yaml
ui-dev:
  image: node:20-alpine
  command: sh -c "npm install && npm run dev -- --host 0.0.0.0"
  volumes:
    - ./demo-ui:/app                      # Mount source code
    - ui_dev_node_modules:/app/node_modules  # Isolate node_modules
  ports:
    - "5173:5173"                         # Expose dev server
```

### Testing HMR

1. Start dev mode: `start-dev.bat`
2. Open http://localhost:5173
3. Edit `demo-ui/src/pages/Home.jsx`
4. Change the hero title text
5. Save the file
6. Watch the browser update automatically!

### Troubleshooting

**HMR Not Working?**

1. **Check the console** for WebSocket connection errors
2. **Verify port 5173** is not blocked by firewall
3. **Restart the container**:
   ```bash
   docker-compose restart ui-dev
   ```
4. **Check file permissions** (Linux/Mac):
   ```bash
   chmod -R 755 demo-ui/src
   ```

**Changes Not Detected?**

1. **Verify volume mount**:
   ```bash
   docker-compose exec ui-dev ls -la /app/src
   ```
2. **Check polling is enabled** in `vite.config.js`
3. **Increase polling interval** if on slow filesystem:
   ```javascript
   watch: {
     usePolling: true,
     interval: 500,  // Increase from 100ms
   }
   ```

**Browser Not Updating?**

1. **Hard refresh**: Ctrl+Shift+R (Windows/Linux) or Cmd+Shift+R (Mac)
2. **Clear browser cache**
3. **Check browser console** for errors
4. **Verify HMR WebSocket** is connected (look for `[vite] connected` message)

### Performance Tips

- **Use Chrome DevTools** to monitor HMR updates
- **Keep dev server running** - don't restart unnecessarily
- **Use React DevTools** to inspect component updates
- **Monitor Docker logs**: `docker-compose logs -f ui-dev`

### Comparison: HMR vs Watch Mode

| Feature | HMR (npm run dev) | Watch Mode (npm run watch) |
|---------|-------------------|----------------------------|
| Update Speed | ⚡ Instant (< 1s) | 🐌 Slow (3-5s) |
| Page Reload | ❌ Not needed | ✅ Required |
| State Preservation | ✅ Keeps state | ❌ Loses state |
| Build Required | ❌ No | ✅ Yes |
| Production-like | ❌ Dev only | ✅ More similar |
| Best For | Development | Testing builds |

**Conclusion**: HMR (`npm run dev`) is the best choice for active development!

---

## Backend Development

The backend does not have hot reload. To apply changes:

1. **Rebuild the container**:
   ```bash
   docker-compose up --build app
   ```

2. **Or develop locally**:
   ```bash
   cd demo-api
   ./mvnw spring-boot:run
   ```
   Spring Boot DevTools can provide some hot reload for Java changes.

---

## Database Changes

To reset the database with fresh seed data:

```bash
# Stop and remove volumes
docker-compose down -v

# Start fresh
docker-compose --profile dev up --build
```

The `init.sql` script only runs when the database is first created.

---

## Logs

**View all logs**:
```bash
docker-compose logs -f
```

**View specific service**:
```bash
docker-compose logs -f ui-dev    # Frontend dev server
docker-compose logs -f app       # Backend API
docker-compose logs -f mysql     # Database
```

**View last 50 lines**:
```bash
docker-compose logs --tail=50 ui-dev
```

---

## Common Development Tasks

### Switch Between Dev and Prod Modes

**Development Mode (HMR enabled)**:
```bash
# Windows
start-dev.bat

# Linux/Mac
./start-dev.sh

# Manual
docker-compose --profile dev up --build
```

**Production Mode (nginx serving built files)**:
```bash
# Windows
start-prod.bat

# Linux/Mac
./start-prod.sh

# Manual
docker-compose --profile prod up --build
```

**Important**: The `ui` (production) and `ui-dev` (development) containers use different profiles to prevent port conflicts. Only one UI container runs at a time.

### Add a New NPM Package

```bash
# Stop the dev container
docker-compose stop ui-dev

# Add the package
cd demo-ui
npm install <package-name>

# Restart dev container (will reinstall packages)
docker-compose up -d ui-dev
```

### Update Dependencies

```bash
cd demo-ui
npm update
npm audit fix

# Rebuild
docker-compose up --build ui-dev
```

### Run Tests

**Frontend**:
```bash
cd demo-ui
npm test                # Run once
npm run test:watch      # Watch mode
```

**Backend**:
```bash
cd demo-api
./mvnw test
```

### Format Code

**Frontend**:
```bash
cd demo-ui
npm run lint:fix
```

**Backend**:
```bash
cd demo-api
./mvnw spotless:apply
```

---

## IDE Setup

### VS Code

Recommended extensions:
- ESLint
- Prettier
- Vite
- Docker
- Java Extension Pack

### IntelliJ IDEA

- Enable Spring Boot support
- Configure Spotless plugin
- Enable Docker integration

---

## Production Build Testing

To test the production build locally:

```bash
# Build and start production mode
docker-compose up --build

# Access at http://localhost:3000
```

This uses nginx to serve the built React app, similar to production deployment.
