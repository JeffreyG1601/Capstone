// src/App.jsx
import React from "react";
import { BrowserRouter, Routes, Route, Link, Navigate } from "react-router-dom";
import InventoryDashboard from "./components/InventoryDashboard";
import PlannerDashboard from "./components/PlannerDashboard";

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-gray-100">
        {/* Simple Top Navigation */}
        <nav className="bg-white shadow p-4 flex justify-between">
          <h1 className="text-lg font-bold">Network Inventory Management</h1>
          <div className="space-x-4">
            <Link to="/inventory" className="text-blue-600 hover:underline">
              Inventory Manager
            </Link>
            <Link to="/planner" className="text-green-600 hover:underline">
              Planner
            </Link>
          </div>
        </nav>

        {/* Page Routes */}
        <main className="p-4">
          <Routes>
            <Route path="/" element={<Navigate to="/inventory" replace />} />
            <Route path="/inventory" element={<InventoryDashboard />} />
            <Route path="/planner" element={<PlannerDashboard />} />
            <Route
              path="*"
              element={
                <div className="text-center mt-8">
                  <h2 className="text-xl font-semibold">404 - Page Not Found</h2>
                  <Link
                    to="/"
                    className="text-blue-600 underline hover:text-blue-800"
                  >
                    Go Home
                  </Link>
                </div>
              }
            />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
