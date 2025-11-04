// src/contexts/AuthContext.jsx
import React, { createContext, useContext, useState, useEffect } from "react";
import axios from "../services/axiosInstance";

const AuthContext = createContext(null);

// Map backend role values to frontend constants if needed
const ROLE_MAP = {
  ADMIN: "ADMIN",
  "ROLE_ADMIN": "ADMIN",
  INVENTORY_MANAGER: "INVENTORY_MANAGER",
  NETWORK_PLANNER: "NETWORK_PLANNER",
  TECHNICIAN: "TECHNICIAN",
  SUPPORT_AGENT: "SUPPORT_AGENT",
  PLANNER_MANAGER: "PLANNER_MANAGER",
};

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("nims_user"));
    } catch {
      return null;
    }
  });
  const [token, setToken] = useState(() => localStorage.getItem("nims_token"));
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (token) axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    else delete axios.defaults.headers.common["Authorization"];
  }, [token]);

  async function login(email, password) {
    setLoading(true);
    try {
      // backend: POST /api/auth/login -> { token, user }
      const res = await axios.post("/auth/login", { email, password });
      const { token: t, user: u } = res.data;
      const mappedRole = ROLE_MAP[u.role] || u.role;
      const userWithRole = { ...u, role: mappedRole };
      setToken(t);
      setUser(userWithRole);
      localStorage.setItem("nims_token", t);
      localStorage.setItem("nims_user", JSON.stringify(userWithRole));
      axios.defaults.headers.common["Authorization"] = `Bearer ${t}`;
      return userWithRole;
    } finally {
      setLoading(false);
    }
  }

  function logout() {
    setToken(null);
    setUser(null);
    localStorage.removeItem("nims_token");
    localStorage.removeItem("nims_user");
    delete axios.defaults.headers.common["Authorization"];
  }

  return (
    <AuthContext.Provider value={{ user, token, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
