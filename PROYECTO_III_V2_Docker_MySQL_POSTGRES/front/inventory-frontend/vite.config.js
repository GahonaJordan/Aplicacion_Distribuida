import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      // API Gateway para endpoints de negocio
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      // OAuth Server para autenticación (NO pasa por gateway)
      '/auth': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false
      },
      '/oauth2': {
        target: 'http://localhost:9000',
        changeOrigin: true,
        secure: false
      }
    }
  }
})