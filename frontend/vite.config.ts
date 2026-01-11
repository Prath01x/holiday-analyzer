import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // Load env file based on `mode` in the current directory.
  // Set the third parameter to '' to load all env regardless of the `VITE_` prefix.
  const env = loadEnv(mode, process.cwd(), '')

  return {
    plugins: [react()],
    server: {
      port: 5173,
      host: true,
      // Only configure proxy in development
      proxy: mode === 'development' ? {
        '/api': {
          target: 'http://localhost:8080',
          changeOrigin: true,
        }
      } : undefined
    },
    // Ensure environment variables are properly exposed to the client
    define: {
      'import.meta.env.VITE_API_BASE_URL': JSON.stringify(env.VITE_API_BASE_URL || 'http://backend-service:8080')
    }
  }
})