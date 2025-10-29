// src/contexts/QueryClientProvider.jsx
import React from 'react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: { refetchOnWindowFocus: false, retry: 1 },
    mutations: { retry: 0 }
  }
})

export default function AppQueryProvider({ children }) {
  return <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
}
