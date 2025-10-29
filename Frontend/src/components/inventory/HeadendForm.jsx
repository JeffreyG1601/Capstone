// src/components/inventory/HeadendForm.jsx
import React, { useEffect, useState } from "react";

export default function HeadendForm({ isOpen, onClose, onCreate }) {
  const [form, setForm] = useState({ id: "", name: "", location: "", region: "" });

  useEffect(() => { if (isOpen) setForm({ id: "", name: "", location: "", region: "" }); }, [isOpen]);

  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/30">
      <div className="bg-white w-full max-w-md p-4 rounded shadow">
        <div className="flex justify-between items-center mb-3"><h3 className="text-lg font-semibold">Add Headend</h3><button onClick={onClose}>×</button></div>
        <div className="space-y-3">
          <div><label className="block text-sm">Headend ID</label><input value={form.id} onChange={e=>setForm(f=>({...f, id: e.target.value}))} className="w-full border px-2 py-1 rounded" /></div>
          <div><label className="block text-sm">Name</label><input value={form.name} onChange={e=>setForm(f=>({...f, name: e.target.value}))} className="w-full border px-2 py-1 rounded" /></div>
          <div><label className="block text-sm">Location</label><input value={form.location} onChange={e=>setForm(f=>({...f, location: e.target.value}))} className="w-full border px-2 py-1 rounded" /></div>
          <div><label className="block text-sm">Region</label><input value={form.region} onChange={e=>setForm(f=>({...f, region: e.target.value}))} className="w-full border px-2 py-1 rounded" /></div>

          <div className="flex justify-end gap-2">
            <button className="px-3 py-1 bg-gray-200 rounded" onClick={onClose}>Cancel</button>
            <button className="px-3 py-1 bg-indigo-600 text-white rounded" onClick={() => { onCreate({ id: form.id, name: form.name, location: form.location, region: form.region }); }}>Create</button>
          </div>
        </div>
      </div>
    </div>
  );
}
