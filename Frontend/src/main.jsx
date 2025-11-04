// src/main.jsx
import React from "react";
import { createRoot } from "react-dom/client";
import App from "./App";
import "./index.css"; // keep if present

// Router
import { BrowserRouter } from "react-router-dom";

// React Query
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

// Create single QueryClient instance for the app
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 30, // 30s
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

const root = createRoot(document.getElementById("root"));

root.render(
  <React.StrictMode>
    {/* QueryClientProvider at the very top so all children have access */}
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  </React.StrictMode>
);
