// vite.config.js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { fileURLToPath } from 'url'
import { resolve } from 'path'

const __dirname = fileURLToPath(new URL('.', import.meta.url))

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api': 'http://localhost:8080'
    }
  },
  // do not pre-include @tanstack packages here. let npm resolution and dedupe handle them.
  resolve: {
    alias: {
      react: resolve(__dirname, 'node_modules/react'),
      'react-dom': resolve(__dirname, 'node_modules/react-dom'),
      // keep JSX runtime mapping
      'react/compiler-runtime': 'react/jsx-runtime'
    },
    dedupe: ['react', 'react-dom']
  }
})
