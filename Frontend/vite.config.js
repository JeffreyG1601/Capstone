import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    // Use the default plugin-react transform (remove custom babel plugin)
    react(),
  ],
  server: {
    proxy: {
      '/api': 'http://localhost:8080'
    }
  },
   resolve: {
    alias: {
      // Redirect any import of `react/compiler-runtime` to the standard runtime
      "react/compiler-runtime": "react/jsx-runtime"
    }
  }
})
