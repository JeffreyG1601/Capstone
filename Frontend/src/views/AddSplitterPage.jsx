// src/views/AddSplitterPage.jsx
import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function AddSplitterPage() {
  const [form, setForm] = useState({ splitterId: "", model: "", portCapacity: 8, usedPorts: 0, location: "", fiberDistributionHubId: "" });
  const [busy, setBusy] = useState(false);
  const navigate = useNavigate();

  const handleCreate = async (e) => {
    e.preventDefault();
    setBusy(true);
    try {
      await fetch('/api/splitters', { method: 'POST', headers: { 'Content-Type':'application/json' }, body: JSON.stringify(form) });
      navigate('/inventory');
    } catch (err) {
      console.error(err);
      alert('Create failed: ' + (err?.message || err));
    } finally { setBusy(false); }
  };

  return (
    <div className="p-6">
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-semibold">Create Splitter</h1>
        <Link to="/inventory/add" className="btn-ghost">Back</Link>
      </div>

      <div className="card max-w-md">
        <form onSubmit={handleCreate} className="space-y-3">
          <div>
            <label className="form-label">Splitter ID</label>
            <input value={form.splitterId} onChange={e=>setForm(f=>({...f, splitterId: e.target.value}))} />
          </div>
          <div>
            <label className="form-label">Model</label>
            <input value={form.model} onChange={e=>setForm(f=>({...f, model: e.target.value}))} />
          </div>
          <div>
            <label className="form-label">Port Capacity</label>
            <input type="number" value={form.portCapacity} onChange={e=>setForm(f=>({...f, portCapacity: Number(e.target.value)}))} />
          </div>
          <div>
            <label className="form-label">Location</label>
            <input value={form.location} onChange={e=>setForm(f=>({...f, location: e.target.value}))} />
          </div>
          <div>
            <label className="form-label">FDH ID (optional)</label>
            <input value={form.fiberDistributionHubId} onChange={e=>setForm(f=>({...f, fiberDistributionHubId: e.target.value}))} />
          </div>

          <div className="form-actions">
            <button type="button" className="btn-cancel" onClick={() => navigate('/inventory')}>Cancel</button>
            <button type="submit" className="btn-primary" disabled={busy}>{busy ? 'Creating…' : 'Create Splitter'}</button>
          </div>
        </form>
      </div>
    </div>
  );
}
