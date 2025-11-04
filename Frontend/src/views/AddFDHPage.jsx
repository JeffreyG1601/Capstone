// src/views/AddFDHPage.jsx
import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function AddFDHPage() {
  const [form, setForm] = useState({ id: "", name: "", location: "", region: "", maxPorts: 64, headendId: "" });
  const [busy, setBusy] = useState(false);
  const navigate = useNavigate();

  const handleCreate = async (e) => {
    e.preventDefault();
    setBusy(true);
    try {
      await fetch('/api/fdhs', { method: 'POST', headers: { 'Content-Type':'application/json' }, body: JSON.stringify(form) });
      navigate('/inventory');
    } catch (err) {
      console.error(err);
      alert('Create failed: ' + (err?.message || err));
    } finally { setBusy(false); }
  };

  return (
    <div className="p-6">
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-semibold">Create FDH</h1>
        <Link to="/inventory/add" className="btn-ghost">Back</Link>
      </div>

      <div className="card max-w-md">
        <form onSubmit={handleCreate} className="space-y-3">
          <div>
            <label className="form-label">FDH ID</label>
            <input value={form.id} onChange={e=>setForm(f=>({...f, id: e.target.value}))} />
          </div>
          <div>
            <label className="form-label">Name</label>
            <input value={form.name} onChange={e=>setForm(f=>({...f, name: e.target.value}))} />
          </div>
          <div>
            <label className="form-label">Location</label>
            <input value={form.location} onChange={e=>setForm(f=>({...f, location: e.target.value}))} />
          </div>
          <div>
            <label className="form-label">Region</label>
            <input value={form.region} onChange={e=>setForm(f=>({...f, region: e.target.value}))} />
          </div>
          <div>
            <label className="form-label">Max Ports</label>
            <input type="number" value={form.maxPorts} onChange={e=>setForm(f=>({...f, maxPorts: Number(e.target.value)}))} />
          </div>
          <div>
            <label className="form-label">Headend ID (optional)</label>
            <input value={form.headendId} onChange={e=>setForm(f=>({...f, headendId: e.target.value}))} />
          </div>

          <div className="form-actions">
            <button type="button" className="btn-cancel" onClick={() => navigate('/inventory')}>Cancel</button>
            <button type="submit" className="btn-primary" disabled={busy}>{busy ? 'Creating…' : 'Create FDH'}</button>
          </div>
        </form>
      </div>
    </div>
  );
}
