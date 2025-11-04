// src/App.jsx
import React from "react";
import { Routes, Route, Link, Navigate, useParams } from "react-router-dom";

import InventoryDashboard from "./controllers/InventoryController";
import InventoryAddView from "./views/InventoryAddView";
import AddAssetPage from "./views/AddAssetPage";
import AddHeadendPage from "./views/AddHeadendPage";
import AddFDHPage from "./views/AddFDHPage";
import AddSplitterPage from "./views/AddSplitterPage";
import PlannerDashboard from "./controllers/PlannerController";
import TechnicianDashboard from "./controllers/TechnicianController";
import AdminAuditController from "./controllers/AdminAuditController";
import AdminRolesController from "./controllers/AdminRolesController";
import "./styles.css";

// Auth & pages
import ProtectedRoute from "./components/ProtectedRoute";
import LoginPage from "./pages/LoginPage";

// Technician route handling (kept identical)
function TechnicianRoute() {
  const { id } = useParams();
  const normalizedId = id ? id.toString().toLowerCase() : null;
  if (normalizedId) return <TechnicianDashboard techIdProp={Number(normalizedId)} />;

  const stored = localStorage.getItem("techId");
  const normalizedStored = stored ? stored.toString().toLowerCase() : null;
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

// App routes definition
function AppRoutes() {
  return (
    <div className="app-root">
      <nav className="bg-white shadow p-4 flex justify-between">
        <div className="app-nav" style={{ display: "flex", alignItems: "center", gap: 12 }}>
          <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
            <h1 className="app-title">Network Inventory</h1>
            <div className="small-muted">Management Console</div>
          </div>
          <div className="nav-links" style={{ display: "flex", gap: 12, marginLeft: 24 }}>
            <Link to="/inventory" className="btn-link">Inventory</Link>
            <Link to="/planner" className="btn-link secondary">Planner</Link>
            <Link to="/technician/1" className="btn-link">Technician</Link>
            <div className="nav-admin" style={{ marginLeft: 12 }}>
              <Link to="/admin/audit" className="btn-ghost">Admin Audit</Link>
              <Link to="/admin/roles" className="btn-ghost">Admin Roles</Link>
            </div>
          </div>
        </div>
      </nav>

      <main className="app-main">
        <Routes>
          {/* public */}
          <Route path="/login" element={<LoginPage />} />

          {/* protected */}
          <Route path="/" element={
            <ProtectedRoute>
              <Navigate to="/inventory" replace />
            </ProtectedRoute>
          } />

          <Route path="/inventory" element={
            <ProtectedRoute>
              <InventoryDashboard />
            </ProtectedRoute>
          } />
          <Route path="/inventory/add" element={
            <ProtectedRoute>
              <InventoryAddView />
            </ProtectedRoute>
          } />
          <Route path="/inventory/add/asset" element={
            <ProtectedRoute>
              <AddAssetPage />
            </ProtectedRoute>
          } />
          <Route path="/inventory/add/headend" element={
            <ProtectedRoute>
              <AddHeadendPage />
            </ProtectedRoute>
          } />
          <Route path="/inventory/add/fdh" element={
            <ProtectedRoute>
              <AddFDHPage />
            </ProtectedRoute>
          } />
          <Route path="/inventory/add/splitter" element={
            <ProtectedRoute>
              <AddSplitterPage />
            </ProtectedRoute>
          } />

          <Route path="/planner" element={
            <ProtectedRoute>
              <PlannerDashboard />
            </ProtectedRoute>
          } />

          <Route path="/technician/:id" element={
            <ProtectedRoute>
              <TechnicianRoute />
            </ProtectedRoute>
          } />
          <Route path="/technician" element={
            <ProtectedRoute>
              <TechnicianRoute />
            </ProtectedRoute>
          } />

          <Route path="/admin/audit" element={
            <ProtectedRoute>
              <AdminAuditController />
            </ProtectedRoute>
          } />
          <Route path="/admin/roles" element={
            <ProtectedRoute>
              <AdminRolesController />
            </ProtectedRoute>
          } />

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
  );
}

export default function App() {
  // Providers (Router + QueryClientProvider) are now set in src/main.jsx — App should only render routes
  return <AppRoutes />;
}
