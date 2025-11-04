// src/pages/LoginPage.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

export default function LoginPage() {
  const { login, loading } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const nav = useNavigate();

  async function submit(e) {
    e.preventDefault();
    setError(null);
    try {
      await login(email, password);
      nav("/");
    } catch (err) {
      const msg = err?.message || err?.detail || err?.error || "Login failed";
      setError(msg);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 p-4">
      <form onSubmit={submit} className="w-full max-w-md bg-white p-6 rounded-2xl shadow">
        <h1 className="text-2xl font-semibold mb-4">NIMS Sign in</h1>
        <label className="block text-sm">Email</label>
        <input autoFocus value={email} onChange={(e) => setEmail(e.target.value)} className="w-full p-2 border rounded mt-1 mb-3" />
        <label className="block text-sm">Password</label>
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} className="w-full p-2 border rounded mt-1 mb-3" />
        {error && <div className="text-red-600 text-sm mb-2">{error}</div>}
        <button type="submit" disabled={loading} className="w-full py-2 rounded bg-slate-800 text-white">
          {loading ? "Signing..." : "Sign in"}
        </button>
        <div className="text-xs text-gray-500 mt-3">Use your project credentials. Backend expected POST /api/auth/login.</div>
      </form>
    </div>
  );
}
