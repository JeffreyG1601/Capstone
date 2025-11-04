// src/views/InventoryAddView.jsx
import React from "react";
import { Link } from "react-router-dom";

export default function InventoryAddView() {
  return (
    <div className="p-6">
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-semibold">Add Infrastructure / Asset</h1>
        <Link to="/inventory" className="btn-ghost">Back to Inventory</Link>
      </div>

      <div className="grid" style={{ gridTemplateColumns: "repeat(3, 1fr)", gap: 16 }}>
        <Link to="/inventory/add/asset" className="card" style={{ textAlign: "center", padding: 20 }}>
          <h3 className="text-lg font-semibold">Add Asset</h3>
          <p className="small-muted mt-2">Create ONT, Router, Switch or other inventory items</p>
        </Link>

        <Link to="/inventory/add/headend" className="card" style={{ textAlign: "center", padding: 20 }}>
          <h3 className="text-lg font-semibold">Add Headend</h3>
          <p className="small-muted mt-2">Create a headend location</p>
        </Link>

        <Link to="/inventory/add/fdh" className="card" style={{ textAlign: "center", padding: 20 }}>
          <h3 className="text-lg font-semibold">Add FDH</h3>
          <p className="small-muted mt-2">Create a Fiber Distribution Hub</p>
        </Link>

        <Link to="/inventory/add/splitter" className="card" style={{ textAlign: "center", padding: 20 }}>
          <h3 className="text-lg font-semibold">Add Splitter</h3>
          <p className="small-muted mt-2">Create a fiber splitter</p>
        </Link>
      </div>
    </div>
  );
}
