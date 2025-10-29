// src/App.jsx
import React from "react";
import { BrowserRouter, Routes, Route, Link, Navigate, useParams } from "react-router-dom";
import InventoryDashboard from "./controllers/InventoryController";
import PlannerDashboard from "./controllers/PlannerController";
import TechnicianDashboard from "./controllers/TechnicianController";
import AdminAuditController from "./controllers/AdminAuditController";
import AdminRolesController from "./controllers/AdminRolesController";

function TechnicianRoute() {
  const { id } = useParams();
  const normalizedId = id ? id.toLowerCase() : null;
  if (normalizedId) return <TechnicianDashboard techIdProp={Number(normalizedId)} />;

  const stored = localStorage.getItem("techId");
  const normalizedStored = stored ? stored.toLowerCase() : null;
  if (normalizedStored) return <TechnicianDashboard techIdProp={Number(normalizedStored)} />;

  return (
    <div className="p-6 text-center">
      <h2 className="text-lg font-semibold">Technician</h2>
      <p className="text-sm text-gray-600 mt-2">
        No technician id provided. Use login flow or visit{" "}
        <Link to="/technician/1" className="underline text-blue-600">
          /technician/1
        </Link>{" "}
        to test.
      </p>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-gray-100">
        <nav className="bg-white shadow p-4 flex justify-between">
          <h1 className="text-lg font-bold">Network Inventory Management</h1>
          <div className="space-x-4">
            <Link to="/inventory" className="text-blue-600 hover:underline">Inventory Manager</Link>
            <Link to="/planner" className="text-green-600 hover:underline">Planner</Link>
            <Link to="/technician/1" className="text-indigo-600 hover:underline">Technician</Link>
            <Link to="/admin/audit" className="text-red-600 hover:underline">Admin Audit</Link>
            <Link to="/admin/roles" className="text-red-600 hover:underline">Admin Roles</Link>
          </div>
        </nav>

        <main className="p-4">
          <Routes>
            <Route path="/" element={<Navigate to="/inventory" replace />} />
            <Route path="/inventory" element={<InventoryDashboard />} />
            <Route path="/planner" element={<PlannerDashboard />} />
            <Route path="/technician/:id" element={<TechnicianRoute />} />
            <Route path="/technician" element={<TechnicianRoute />} />
            <Route path="/admin/audit" element={<AdminAuditController />} />
            <Route path="/admin/roles" element={<AdminRolesController />} />
            <Route
              path="*"
              element={
                <div className="text-center mt-8">
                  <h2 className="text-xl font-semibold">404 - Page Not Found</h2>
                  <Link to="/" className="text-blue-600 underline hover:text-blue-800">Go Home</Link>
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
