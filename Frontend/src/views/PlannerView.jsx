// src/views/PlannerView.jsx
import React from "react";

export default function PlannerView({ fdhs, splitters, technicians, form, setForm, fetchSplitters, loading, error }) {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-semibold mb-4">Planner — Onboarding</h1>

      {error && <div className="mb-3 text-red-600">{error}</div>}
      {loading && <div className="mb-3">Loading...</div>}

      <form className="space-y-3 max-w-2xl">
        <div>
          <label className="block text-sm">Customer name</label>
          <input value={form.name} onChange={e=>setForm(f=>({...f, name: e.target.value}))} className="w-full border px-2 py-1 rounded" />
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-sm">Address</label>
            <input value={form.address} onChange={e=>setForm(f=>({...f, address: e.target.value}))} className="w-full border px-2 py-1 rounded" />
          </div>
          <div>
            <label className="block text-sm">Neighborhood</label>
            <input value={form.neighborhood} onChange={e=>setForm(f=>({...f, neighborhood: e.target.value}))} className="w-full border px-2 py-1 rounded" />
          </div>
        </div>

        <div className="grid grid-cols-3 gap-3">
          <div>
            <label className="block text-sm">FDH</label>
            <select value={form.fdhId} onChange={e => { setForm(f => ({ ...f, fdhId: e.target.value })); fetchSplitters(e.target.value); }} className="w-full border px-2 py-1 rounded">
              <option value="">Select FDH</option>
              {fdhs.map(f => <option key={f.fdhId} value={f.fdhId}>{f.name ?? f.displayName} ({f.region ?? f.area})</option>)}
            </select>
          </div>

          <div>
            <label className="block text-sm">Splitter</label>
            <select value={form.splitterId} onChange={e=>setForm(f=>({...f, splitterId: e.target.value}))} className="w-full border px-2 py-1 rounded">
              <option value="">Select Splitter</option>
              {splitters.map(s => <option key={s.splitterId} value={s.splitterId}>{s.model ?? (`Splitter ${s.splitterId}`)}</option>)}
            </select>
          </div>

          <div>
            <label className="block text-sm">Technician</label>
            <select value={form.technicianId} onChange={e=>setForm(f=>({...f, technicianId: e.target.value}))} className="w-full border px-2 py-1 rounded">
              <option value="">Select Technician</option>
              {technicians.map(t => <option key={t.technicianId} value={t.technicianId}>{t.name}</option>)}
            </select>
          </div>
        </div>
      </form>
    </div>
  );
}
