// src/components/inventory/AssetStatusBadge.jsx
import React from "react";

export default function AssetStatusBadge({ status }) {
  const s = (status || "UNKNOWN").toUpperCase();
  const map = {
    AVAILABLE: "bg-green-100 text-green-800",
    ASSIGNED: "bg-yellow-100 text-yellow-800",
    FAULTY: "bg-red-100 text-red-800",
    RETIRED: "bg-gray-100 text-gray-700",
    UNKNOWN: "bg-gray-50 text-gray-700"
  };
  const cls = map[s] || map.UNKNOWN;
  return <span className={`px-2 py-0.5 rounded text-xs ${cls}`}>{s}</span>;
}
