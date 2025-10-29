// src/components/inventory/SplitterForm.jsx
import React, { useEffect, useState } from "react";

export default function SplitterForm({ isOpen, onClose, onCreate }) {
  const [form, setForm] = useState({ splitterId: "", model: "", portCapacity: 8, usedPorts: 0, location: "", fiberDistributionHubId: "" });

  useEffect(() => { if (isOpen) setForm({ splitterId: "", model: "", portCapacity: 8, usedPorts: 0, location: "", fiberDistributionHubId: "" }); }, [isOpen]);

  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/30">
      <div className="bg-white w-full max-w-md p-4 rounded shadow">
        <div className="flex justify-between items-center mb-3"><h3 className="text-lg font-semibold">Add Splitter</h3><button onClick={onClose}>×</button></div>

        <div className="space-y-3">
          <div><label className="block text-sm">Splitter ID</label><input value={form.splitterId} onChange={e=>setForm(f=>({...f, splitterId: e.target.value}))} className="w-full border px-2 py-1 rounded" /></div>
          <div><label className="block text-sm">Model</label><input value={form.model} onChange={e=>setForm(f=>({...f, model: e.target.value}))} className="w-full border px-2 py-1 rounded" /></div>
          <div><label className="block text-sm">Port Capacity</label><input type="number" value={form.portCapacity} onChange={e=>setForm(f=>({...f, portCapacity: Number(e.target.value)}))} className="w-full border px-2 py-1 rounded" /></div>
          <div><label className="block text-sm">Location</label><input value={form.location} onChange={e=>setForm(f=>({...f, location: e.target.value}))} className="w-full border px-2 py-1 rounded" /></div>
          <div><label className="block text-sm">FDH ID (optional)</label><input value={form.fiberDistributionHubId} onChange={e=>setForm(f=>({...f, fiberDistributionHubId: e.target.value}))} className="w-full border px-2 py-1 rounded" /></div>

          <div className="flex justify-end gap-2">
            <button className="px-3 py-1 bg-gray-200 rounded" onClick={onClose}>Cancel</button>
            <button className="px-3 py-1 bg-indigo-600 text-white rounded" onClick={() => { onCreate(form); }}>Create</button>
          </div>
        </div>
      </div>
    </div>
  );
}
