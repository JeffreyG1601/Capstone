import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "../services/axiosInstance";
import BulkUploadModal from "../components/inventory/BulkUploadModal";

export default function AddAssetPage() {
  const [form, setForm] = useState({ assetType: "ONT", model: "", serialNumber: "", status: "AVAILABLE", location: "" });
  const [busy, setBusy] = useState(false);
  const [bulkOpen, setBulkOpen] = useState(false);
  const [uploading, setUploading] = useState(false);
  const navigate = useNavigate();

  const handleCreate = async (e) => {
    e.preventDefault();
    if (!form.model || !form.serialNumber) return alert("Model and Serial are required.");
    setBusy(true);
    try {
      await axios.post(`/assets`, form);
      navigate("/inventory");
    } catch (err) {
      console.error(err);
      alert("Create failed: " + (err?.response?.data || err?.message || err));
    } finally {
      setBusy(false);
    }
  };

  // Download CSV template
  const downloadTemplate = () => {
    const header = "assetType,model,serialNumber,status,location\n";
    const sample = [
      "ONT,HG8245H,ONT-001,AVAILABLE,Warehouse A",
      "ROUTER,TP-WR841N,RT-102,AVAILABLE,Warehouse A",
      "ETHERNET_SWITCH,DS-24,SW-55,AVAILABLE,Storage Room"
    ].join("\n");
    const csv = header + sample;
    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "assets-template.csv";
    document.body.appendChild(a);
    a.click();
    a.remove();
    URL.revokeObjectURL(url);
  };

  // Handler called by BulkUploadModal with parsed rows (array of {assetType, model, serialNumber, status, location})
  const handleBulkUpload = async (rows) => {
    if (!Array.isArray(rows) || rows.length === 0) {
      return alert("No rows supplied to upload.");
    }
    // Basic client-side validation: require model & serialNumber on each row
    const invalid = rows.find((r) => !r.model || !r.serialNumber);
    if (invalid) return alert("Every row must have both model and serialNumber. Fix your CSV and try again.");

    setUploading(true);
    try {
      // NOTE: backend endpoint not implemented yet? This posts JSON array to /api/assets/bulk
      // Your backend can accept application/json and process the array.
      const res = await axios.post(`/assets/bulk`, rows);
      alert("Bulk upload successful. Inserted: " + (res?.data?.insertedCount ?? rows.length));
      setBulkOpen(false);
      navigate("/inventory");
    } catch (err) {
      console.error("Bulk upload failed", err);
      // Show server error message if present
      const msg = err?.response?.data || err?.message || "Unknown error";
      alert("Bulk upload failed: " + msg);
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="p-6">
      <BulkUploadModal isOpen={bulkOpen} onClose={() => setBulkOpen(false)} onUpload={handleBulkUpload} />

      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-semibold">Create Asset</h1>
        <Link to="/inventory" className="btn-ghost">Back</Link>
      </div>

      <div className="mb-4 flex gap-3">
        <button type="button" className="btn-outline" onClick={downloadTemplate}>
          ⤓ Download CSV template
        </button>

        <button type="button" className="btn-primary" onClick={() => setBulkOpen(true)}>
          ⤴ Bulk Upload (CSV)
        </button>
      </div>

      <div className="card max-w-md">
        <form onSubmit={handleCreate} className="space-y-3">
          <div>
            <label className="form-label">Asset Type</label>
            <select value={form.assetType} onChange={(e)=> setForm(f=>({...f, assetType: e.target.value}))} className="w-full">
              <option value="ONT">ONT</option>
              <option value="ROUTER">Router</option>
              <option value="ETHERNET_SWITCH">Ethernet Switch</option>
              <option value="SPLITTER">Splitter</option>
              <option value="FDH">FDH</option>
              <option value="HEADEND">Headend</option>
            </select>
          </div>

          <div>
            <label className="form-label">Model</label>
            <input value={form.model} onChange={(e)=> setForm(f=>({...f, model: e.target.value}))} />
          </div>

          <div>
            <label className="form-label">Serial Number</label>
            <input value={form.serialNumber} onChange={(e)=> setForm(f=>({...f, serialNumber: e.target.value}))} />
          </div>

          <div>
            <label className="form-label">Status</label>
            <select value={form.status} onChange={(e)=> setForm(f=>({...f, status: e.target.value}))}>
              <option value="AVAILABLE">Available</option>
              <option value="ASSIGNED">Assigned</option>
              <option value="FAULTY">Faulty</option>
              <option value="RETIRED">Retired</option>
            </select>
          </div>

          <div>
            <label className="form-label">Location</label>
            <input value={form.location} onChange={(e)=> setForm(f=>({...f, location: e.target.value}))} />
          </div>

          <div className="form-actions">
            <button type="button" className="btn-cancel" onClick={() => navigate('/inventory')}>Cancel</button>
            <button type="submit" className="btn-primary" disabled={busy || uploading}>{busy ? 'Creating…' : uploading ? 'Uploading…' : 'Create Asset'}</button>
          </div>
        </form>
      </div>
    </div>
  );
}
