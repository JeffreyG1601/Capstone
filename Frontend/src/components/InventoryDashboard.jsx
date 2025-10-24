import React, { useEffect, useState, useMemo } from "react";

/**
 * InventoryDashboard.jsx
 * Barebones Inventory Manager dashboard (React + Vite)
 *
 * Usage:
 *  - Put this file under src/pages/InventoryDashboard.jsx
 *  - Add a route to it in your router (e.g., /inventory)
 *  - Ensure Tailwind is available (or remove classes)
 *
 * Notes:
 *  - This UI assumes your backend is served at the same origin under /api/*
 *  - If your backend runs on a different host/port, set API_BASE accordingly
 */

const API_BASE = ""; // set to "" (same origin) or "http://localhost:8080" if needed

export default function InventoryDashboard() {
  const [assets, setAssets] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filter, setFilter] = useState({ type: "", status: "", location: "", q: "" });
  const [showCreate, setShowCreate] = useState(false);
  const [createForm, setCreateForm] = useState({
    assetType: "",
    model: "",
    serialNumber: "",
    status: "Available",
    location: "",
  });
  const [bulkFile, setBulkFile] = useState(null);
  const [pageSize] = useState(200); // simple client-side paging limit for tiny lists
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAssets();
  }, []);

  async function fetchAssets() {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/assets`);
      if (!res.ok) throw new Error(`Failed to fetch assets (${res.status})`);
      const data = await res.json();
      setAssets(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || "Unknown error");
    } finally {
      setLoading(false);
    }
  }

  const filtered = useMemo(() => {
    const q = filter.q?.trim().toLowerCase();
    return assets
      .filter((a) => (filter.type ? a.assetType === filter.type : true))
      .filter((a) => (filter.status ? (a.status || "").toString() === filter.status : true))
      .filter((a) => (filter.location ? (a.location || "").toLowerCase().includes(filter.location.toLowerCase()) : true))
      .filter((a) => (q ? (a.serialNumber || "").toLowerCase().includes(q) || (a.model || "").toLowerCase().includes(q) : true))
      .slice(0, pageSize);
  }, [assets, filter, pageSize]);

  /* ---------- Create single asset ---------- */
  async function handleCreate(e) {
    e.preventDefault();
    setError(null);
    // basic validation
    if (!createForm.assetType || !createForm.serialNumber) {
      setError("assetType and serialNumber are required.");
      return;
    }
    try {
      const res = await fetch(`${API_BASE}/api/assets`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(createForm),
      });
      if (res.status === 409) {
        const msg = await res.text();
        throw new Error(msg || "Duplicate serial");
      }
      if (!res.ok) throw new Error(`Create failed (${res.status})`);
      const created = await res.json();
      setAssets((prev) => [created, ...prev]);
      setShowCreate(false);
      setCreateForm({ assetType: "", model: "", serialNumber: "", status: "Available", location: "" });
    } catch (err) {
      setError(err.message || "Failed to create");
    }
  }

  /* ---------- Bulk CSV upload ---------- */
  async function handleBulkUpload(e) {
    e.preventDefault();
    setError(null);
    if (!bulkFile) { setError("Choose a CSV file first."); return; }
    const fd = new FormData();
    fd.append("file", bulkFile);
    try {
      const res = await fetch(`${API_BASE}/api/assets/bulk-upload`, {
        method: "POST",
        body: fd,
      });
      if (!res.ok) {
        const t = await res.text();
        throw new Error(t || `Upload failed (${res.status})`);
      }
      const result = await res.json();
      // The backend should return created items or a summary. We'll refresh list.
      await fetchAssets();
      alert(result?.message || "Bulk upload finished. Refreshed list.");
      setBulkFile(null);
      e.target.reset();
    } catch (err) {
      setError(err.message || "Bulk upload failed");
    }
  }

  /* ---------- Actions: mark faulty, assign, delete ---------- */
  async function patchAsset(id, payload) {
    setError(null);
    try {
      const res = await fetch(`${API_BASE}/api/assets/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error(`Failed (${res.status})`);
      const updated = await res.json();
      setAssets((prev) => prev.map((a) => (a.assetId === updated.assetId ? updated : a)));
    } catch (err) {
      setError(err.message || "Update failed");
    }
  }

  function markFaulty(asset) {
    const payload = { ...asset, status: "Faulty" };
    patchAsset(asset.assetId, payload);
  }

  function markAvailable(asset) {
    const payload = { ...asset, status: "Available", assignedToCustomer: null, assignedDate: null };
    patchAsset(asset.assetId, payload);
  }

  async function assignToCustomerPrompt(asset) {
    const customerIdStr = prompt("Assign to customerId (enter numeric customerId):");
    if (!customerIdStr) return;
    const customerId = Number(customerIdStr);
    if (!Number.isInteger(customerId)) { alert("Invalid id"); return; }

    // We will update asset.assignedToCustomer to { customerId: X } minimal payload
    const payload = { ...asset, assignedToCustomer: { customerId }, status: "Assigned", assignedDate: new Date().toISOString() };
    await patchAsset(asset.assetId, payload);
  }

  async function deleteAsset(asset) {
    if (!confirm(`Delete asset ${asset.serialNumber}?`)) return;
    try {
      const res = await fetch(`${API_BASE}/api/assets/${asset.assetId}`, { method: "DELETE" });
      if (res.status === 204 || res.ok) {
        setAssets((prev) => prev.filter((a) => a.assetId !== asset.assetId));
      } else {
        throw new Error(`Delete failed (${res.status})`);
      }
    } catch (err) {
      setError(err.message || "Delete failed");
    }
  }

  /* ---------- UI ---------- */
  return (
    <div className="p-6">
      <h1 className="text-2xl font-semibold mb-4">Inventory Manager — Dashboard</h1>

      <div className="flex gap-3 items-center mb-4">
        <button
          className="px-3 py-1 bg-blue-600 text-white rounded"
          onClick={() => setShowCreate(true)}
        >
          + Add Asset
        </button>

        <form onSubmit={(e)=>{e.preventDefault(); fetchAssets();}} className="flex gap-2 items-center">
          <input
            className="border px-2 py-1 rounded"
            placeholder="search serial or model..."
            value={filter.q}
            onChange={(e) => setFilter(f => ({ ...f, q: e.target.value }))}
          />
          <select
            className="border px-2 py-1 rounded"
            value={filter.type}
            onChange={(e) => setFilter(f => ({ ...f, type: e.target.value }))}
          >
            <option value="">All types</option>
            <option value="ONT">ONT</option>
            <option value="Router">Router</option>
            <option value="Splitter">Splitter</option>
            <option value="FDH">FDH</option>
          </select>

          <select
            className="border px-2 py-1 rounded"
            value={filter.status}
            onChange={(e) => setFilter(f => ({ ...f, status: e.target.value }))}
          >
            <option value="">All statuses</option>
            <option value="Available">Available</option>
            <option value="Assigned">Assigned</option>
            <option value="Faulty">Faulty</option>
            <option value="Retired">Retired</option>
          </select>

          <input
            className="border px-2 py-1 rounded"
            placeholder="location..."
            value={filter.location}
            onChange={(e) => setFilter(f => ({ ...f, location: e.target.value }))}
          />
          <button className="px-3 py-1 bg-gray-200 rounded" type="submit">Refresh</button>
        </form>

        <form onSubmit={handleBulkUpload} className="ml-auto flex gap-2 items-center">
          <input
            type="file"
            accept=".csv"
            onChange={(e) => setBulkFile(e.target.files?.[0] ?? null)}
            className="text-sm"
          />
          <button className="px-3 py-1 bg-green-600 text-white rounded" type="submit">Bulk Upload</button>
        </form>
      </div>

      {error && <div className="mb-3 text-red-600">Error: {error}</div>}
      {loading && <div className="mb-3">Loading...</div>}

      <div className="overflow-x-auto border rounded">
        <table className="min-w-full divide-y">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-3 py-2 text-left text-sm">ID</th>
              <th className="px-3 py-2 text-left text-sm">Serial</th>
              <th className="px-3 py-2 text-left text-sm">Type</th>
              <th className="px-3 py-2 text-left text-sm">Model</th>
              <th className="px-3 py-2 text-left text-sm">Status</th>
              <th className="px-3 py-2 text-left text-sm">Location</th>
              <th className="px-3 py-2 text-left text-sm">Assigned To</th>
              <th className="px-3 py-2 text-left text-sm">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white">
            {filtered.map((a) => (
              <tr key={a.assetId} className="hover:bg-gray-50">
                <td className="px-3 py-2 text-sm">{a.assetId}</td>
                <td className="px-3 py-2 text-sm">{a.serialNumber}</td>
                <td className="px-3 py-2 text-sm">{a.assetType}</td>
                <td className="px-3 py-2 text-sm">{a.model}</td>
                <td className="px-3 py-2 text-sm">{a.status}</td>
                <td className="px-3 py-2 text-sm">{a.location}</td>
                <td className="px-3 py-2 text-sm">
                  {a.assignedToCustomer ? `${a.assignedToCustomer.customerId} - ${a.assignedToCustomer.name || ""}` : "---"}
                </td>
                <td className="px-3 py-2 text-sm flex gap-2">
                  <button className="px-2 py-1 bg-yellow-200 rounded" onClick={() => assignToCustomerPrompt(a)}>Assign</button>
                  {a.status !== "Faulty" ? (
                    <button className="px-2 py-1 bg-red-200 rounded" onClick={() => markFaulty(a)}>Mark Faulty</button>
                  ) : (
                    <button className="px-2 py-1 bg-green-200 rounded" onClick={() => markAvailable(a)}>Mark Available</button>
                  )}
                  <button className="px-2 py-1 bg-gray-200 rounded" onClick={() => deleteAsset(a)}>Delete</button>
                </td>
              </tr>
            ))}
            {filtered.length === 0 && !loading && (
              <tr>
                <td className="p-4 text-sm text-gray-500" colSpan="8">No assets found.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Create modal */}
      {showCreate && (
        <div className="fixed inset-0 bg-black/40 flex items-start justify-center p-6">
          <div className="bg-white w-full max-w-md rounded shadow p-4">
            <div className="flex justify-between items-center mb-3">
              <h2 className="text-lg font-medium">Add Asset</h2>
              <button onClick={() => setShowCreate(false)} className="text-gray-600">Close</button>
            </div>

            <form onSubmit={handleCreate} className="space-y-3">
              <div>
                <label className="block text-sm">Asset Type</label>
                <input className="w-full border px-2 py-1 rounded" value={createForm.assetType} onChange={(e)=>setCreateForm(f=>({...f, assetType: e.target.value}))} placeholder="ONT / Router / Splitter" />
              </div>

              <div>
                <label className="block text-sm">Model</label>
                <input className="w-full border px-2 py-1 rounded" value={createForm.model} onChange={(e)=>setCreateForm(f=>({...f, model: e.target.value}))} />
              </div>

              <div>
                <label className="block text-sm">Serial Number</label>
                <input className="w-full border px-2 py-1 rounded" value={createForm.serialNumber} onChange={(e)=>setCreateForm(f=>({...f, serialNumber: e.target.value}))} />
              </div>

              <div>
                <label className="block text-sm">Status</label>
                <select className="w-full border px-2 py-1 rounded" value={createForm.status} onChange={(e)=>setCreateForm(f=>({...f, status: e.target.value}))}>
                  <option>Available</option>
                  <option>Assigned</option>
                  <option>Faulty</option>
                  <option>Retired</option>
                </select>
              </div>

              <div>
                <label className="block text-sm">Location</label>
                <input className="w-full border px-2 py-1 rounded" value={createForm.location} onChange={(e)=>setCreateForm(f=>({...f, location: e.target.value}))} />
              </div>

              <div className="flex justify-end gap-2">
                <button type="button" onClick={() => setShowCreate(false)} className="px-3 py-1 bg-gray-200 rounded">Cancel</button>
                <button type="submit" className="px-3 py-1 bg-blue-600 text-white rounded">Create</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
