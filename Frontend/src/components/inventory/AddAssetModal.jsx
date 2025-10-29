// src/components/inventory/AddAssetModal.jsx
import React, { useEffect, useState } from "react";

export default function AddAssetModal({ isOpen, onClose, onCreate }) {
  const [form, setForm] = useState({
    assetType: "ONT",
    model: "",
    serialNumber: "",
    status: "AVAILABLE",
    location: ""
  });
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    if (isOpen) setForm({ assetType: "ONT", model: "", serialNumber: "", status: "AVAILABLE", location: "" });
  }, [isOpen]);

  if (!isOpen) return null;

  const handleCreate = async () => {
    if (!form.serialNumber || !form.model) return alert("Model and Serial are required.");
    setBusy(true);
    try {
      await onCreate({
        assetType: form.assetType,
        model: form.model,
        serialNumber: form.serialNumber,
        status: form.status,
        location: form.location,
      });
    } catch (e) {
      console.error(e);
      alert("Create failed: " + (e?.message || e));
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/30">
      <div className="bg-white w-full max-w-lg p-4 rounded shadow">
        <div className="flex justify-between items-center mb-3">
          <h3 className="text-lg font-semibold">Add Asset</h3>
          <button onClick={onClose}>×</button>
        </div>

        <div className="space-y-3">
          <div>
            <label className="block text-sm">Asset Type</label>
            <select value={form.assetType} onChange={(e)=> setForm(f=>({...f, assetType: e.target.value}))} className="w-full border px-2 py-1 rounded">
              <option value="ONT">ONT</option>
              <option value="ROUTER">Router</option>
              <option value="ETHERNET_SWITCH">Ethernet Switch</option>
              <option value="SPLITTER">Splitter</option>
              <option value="FDH">FDH</option>
              <option value="HEADEND">Headend</option>
            </select>
          </div>

          <div>
            <label className="block text-sm">Model</label>
            <input value={form.model} onChange={(e)=> setForm(f=>({...f, model: e.target.value}))} className="w-full border px-2 py-1 rounded" />
          </div>

          <div>
            <label className="block text-sm">Serial Number</label>
            <input value={form.serialNumber} onChange={(e)=> setForm(f=>({...f, serialNumber: e.target.value}))} className="w-full border px-2 py-1 rounded" />
          </div>

          <div>
            <label className="block text-sm">Status</label>
            <select value={form.status} onChange={(e)=> setForm(f=>({...f, status: e.target.value}))} className="w-full border px-2 py-1 rounded">
              <option value="AVAILABLE">Available</option>
              <option value="ASSIGNED">Assigned</option>
              <option value="FAULTY">Faulty</option>
              <option value="RETIRED">Retired</option>
            </select>
          </div>

          <div>
            <label className="block text-sm">Location</label>
            <input value={form.location} onChange={(e)=> setForm(f=>({...f, location: e.target.value}))} className="w-full border px-2 py-1 rounded" />
          </div>

          <div className="flex justify-end gap-2">
            <button className="px-3 py-1 bg-gray-200 rounded" onClick={onClose} disabled={busy}>Cancel</button>
            <button className="px-3 py-1 bg-indigo-600 text-white rounded" onClick={handleCreate} disabled={busy}>Create</button>
          </div>
        </div>
      </div>
    </div>
  );
}
