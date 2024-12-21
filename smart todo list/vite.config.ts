import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/journey-genie-backend-api': {
        target: 'http://localhost:8094/',
        changeOrigin: true,
        secure: false,
      }
    }
  }
})