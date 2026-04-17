import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 5173,
    watch: {
      usePolling: true,
    },
    proxy: {
      '/api': 'http://app:8080',
      '/register': 'http://app:8080',
      '/login': 'http://app:8080',
      '/welcome': 'http://app:8080',
    }
  }
})
