# Hot Module Replacement (HMR) Setup

## ✅ Current Configuration

The dev profile is **already configured** with Hot Module Replacement using Vite's dev server. This is **superior to `npm run watch`** because:

### Docker Compose Profiles

The application uses Docker Compose profiles to prevent conflicts between production and development UI containers:

```
┌─────────────────────────────────────────────────────────┐
│                    Docker Compose                        │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  Default (no profile):                                  │
│    ├── mysql (insurance-db)                             │
│    └── app (insurance-app)                              │
│                                                          │
│  --profile prod:                                        │
│    ├── mysql (insurance-db)                             │
│    ├── app (insurance-app)                              │
│    └── ui (insurance-ui) ← nginx serving built files   │
│        Port: 3000                                        │
│                                                          │
│  --profile dev:                                         │
│    ├── mysql (insurance-db)                             │
│    ├── app (insurance-app)                              │
│    └── ui-dev (insurance-ui-dev) ← Vite dev server     │
│        Port: 5173 (with HMR!)                           │
│                                                          │
└─────────────────────────────────────────────────────────┘
```

**Why profiles?**
- Prevents port conflicts between `ui` and `ui-dev`
- Allows switching between prod and dev modes easily
- Only one UI container runs at a time

### HMR vs Watch Mode

| Feature | HMR (Current Setup) | Watch Mode |
|---------|---------------------|------------|
| **Update Speed** | ⚡ < 1 second | 🐌 3-5 seconds |
| **Page Reload** | ❌ Not needed | ✅ Required |
| **State Preservation** | ✅ Keeps component state | ❌ Loses state |
| **Build Step** | ❌ No build needed | ✅ Full rebuild |
| **Developer Experience** | 🎯 Excellent | 😐 Acceptable |

## 🔧 How It Works

### 1. Docker Compose Configuration

```yaml
ui-dev:
  image: node:20-alpine
  command: sh -c "npm install && npm run dev -- --host 0.0.0.0"
  volumes:
    - ./demo-ui:/app                      # Source code mounted
    - ui_dev_node_modules:/app/node_modules  # Isolated node_modules
  ports:
    - "5173:5173"                         # Dev server port
  environment:
    - VITE_API_URL=http://localhost:8080
```

**Key Points:**
- `npm run dev` starts Vite dev server with HMR
- `--host 0.0.0.0` allows connections from outside container
- Volume mount enables file watching
- Named volume for node_modules prevents conflicts

### 2. Vite Configuration

```javascript
// demo-ui/vite.config.js
export default defineConfig({
  server: {
    host: '0.0.0.0',           // Accept external connections
    port: 5173,                 // Dev server port
    strictPort: true,           // Fail if port unavailable
    watch: {
      usePolling: true,         // Required for Docker
      interval: 100,            // Check every 100ms
    },
    hmr: {
      host: 'localhost',        // HMR WebSocket host
      port: 5173,               // HMR WebSocket port
    }
  }
})
```

**Key Points:**
- `usePolling: true` - Essential for Docker file system watching
- `interval: 100` - Fast polling for quick updates
- `hmr` config - WebSocket connection for instant updates

### 3. Package.json Scripts

```json
{
  "scripts": {
    "dev": "vite",              // Starts dev server with HMR
    "build": "vite build",      // Production build
    "preview": "vite preview"   // Preview production build
  }
}
```

## 🚀 Usage

### Start Development Mode

```bash
# Windows
start-dev.bat

# Linux/Mac or Manual
docker-compose --profile dev up --build
```

### Access the Application

- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html

### Make Changes

1. Edit any file in `demo-ui/src/**`
2. Save the file
3. Watch browser update automatically!

**Example:**
- Edit `demo-ui/src/pages/Home.jsx`
- Change the hero title
- Save
- Browser updates instantly without refresh!

## 🎯 What Gets Hot Reloaded

### ✅ Instant Updates (No Refresh)

- React components (`.jsx`, `.js`)
- CSS files (`.css`)
- Context providers
- API client code
- Component state is preserved!

### ⚠️ Requires Manual Refresh

- `index.html` changes
- `vite.config.js` changes
- Environment variables (`.env`)
- New npm packages added

## 🔍 Verification

### Check HMR is Working

1. Open browser console (F12)
2. Look for: `[vite] connected.`
3. Edit a file and save
4. Look for: `[vite] hmr update /src/pages/Home.jsx`

### Check WebSocket Connection

In browser console:
```javascript
// Should show WebSocket connection
ws://localhost:5173/
```

### Monitor Docker Logs

```bash
docker-compose logs -f ui-dev
```

Look for:
```
VITE v7.x.x  ready in xxx ms
➜  Local:   http://localhost:5173/
➜  Network: http://172.x.x.x:5173/
```

## 🐛 Troubleshooting

### HMR Not Working?

**1. Check WebSocket Connection**
- Open browser console
- Look for WebSocket errors
- Verify `ws://localhost:5173/` is connected

**2. Restart Container**
```bash
docker-compose restart ui-dev
```

**3. Check File Permissions** (Linux/Mac)
```bash
chmod -R 755 demo-ui/src
```

**4. Verify Volume Mount**
```bash
docker-compose exec ui-dev ls -la /app/src
```

### Changes Not Detected?

**1. Increase Polling Interval**

Edit `demo-ui/vite.config.js`:
```javascript
watch: {
  usePolling: true,
  interval: 500,  // Increase from 100ms
}
```

**2. Check File System**
- Some file systems (NFS, VirtualBox shared folders) may have issues
- Try native Docker volumes or WSL2 on Windows

**3. Verify Vite is Running**
```bash
docker-compose logs ui-dev | grep "ready in"
```

### Port Conflicts?

**Check what's using port 5173:**

Windows:
```bash
netstat -ano | findstr :5173
```

Linux/Mac:
```bash
lsof -i :5173
```

**Kill the process or change the port** in `docker-compose.yml` and `vite.config.js`.

### Both UI Containers Running?

If you see both `insurance-ui` and `insurance-ui-dev` running:

```bash
# Stop all containers
docker-compose down

# Start only dev profile
docker-compose --profile dev up --build

# Or start only prod profile
docker-compose --profile prod up --build
```

**Check running containers:**
```bash
docker-compose ps
```

You should see either:
- `insurance-ui` (prod) on port 3000, OR
- `insurance-ui-dev` (dev) on port 5173

**Never both at the same time!**

## 📊 Performance

### Typical Update Times

- **Component change**: < 500ms
- **CSS change**: < 200ms
- **Context change**: < 1s
- **Large file**: < 2s

### Optimization Tips

1. **Keep dev server running** - don't restart unnecessarily
2. **Use React DevTools** to monitor updates
3. **Minimize file size** - split large components
4. **Use code splitting** for better performance

## 🎓 Learning Resources

- [Vite HMR API](https://vitejs.dev/guide/api-hmr.html)
- [React Fast Refresh](https://github.com/facebook/react/tree/main/packages/react-refresh)
- [Docker Volumes](https://docs.docker.com/storage/volumes/)

## ✨ Summary

The current setup uses **Vite's HMR** which is:
- ⚡ **Faster** than watch mode
- 🎯 **More precise** - only updates changed modules
- 🔄 **Stateful** - preserves component state
- 🚀 **Production-ready** - battle-tested technology

**No changes needed** - the setup is already optimal for development! 🎉
