// src/components/inventory/BulkUploadModal.jsx
import React, { useState } from "react";

/**
 * Expects CSV with headers (case-insensitive):
 * assetType, model, serialNumber, status, location
 */
export default function BulkUploadModal({ isOpen, onClose, onUpload }) {
  const [csvText, setCsvText] = useState("");
  const [previewRows, setPreviewRows] = useState([]);
  const [error, setError] = useState(null);

  if (!isOpen) return null;

  const parseCsv = (text) => {
    const lines = text.split(/\r?\n/).map(l => l.trim()).filter(Boolean);
    if (lines.length === 0) return [];
    const headers = lines[0].split(",").map(h => h.trim().toLowerCase());
    const rows = lines.slice(1).map(line => {
      const cols = line.split(",").map(c => c.trim());
      const obj = {};
      headers.forEach((h, i) => { obj[h] = cols[i] ?? ""; });
      return {
        assetType: (obj.assettype || obj.type || "").toUpperCase(),
        model: obj.model || "",
        serialNumber: obj.serialnumber || obj.serial || "",
        status: (obj.status || "AVAILABLE").toUpperCase(),
        location: obj.location || ""
      };
    });
    return rows;
  };

  const onFilePicked = (file) => {
    setError(null);
    const reader = new FileReader();
    reader.onload = (e) => {
      setCsvText(e.target.result);
      try {
        const rows = parseCsv(e.target.result);
        setPreviewRows(rows);
      } catch (err) {
        setError("Failed to parse CSV");
      }
    };
    reader.readAsText(file);
  };

  const handleUpload = async () => {
    if (previewRows.length === 0) return alert("No rows to upload.");
    await onUpload(previewRows);
  };

  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/30">
      <div className="bg-white w-full max-w-2xl p-4 rounded shadow">
        <div className="flex justify-between items-center mb-3">
          <h3 className="text-lg font-semibold">Bulk Upload Assets (CSV)</h3>
          <button onClick={onClose}>×</button>
        </div>

        <div className="mb-3">
          <input type="file" accept=".csv,text/csv" onChange={(e)=> { if(e.target.files[0]) onFilePicked(e.target.files[0]) }} />
        </div>

        <div className="mb-3">
          <div className="text-sm text-gray-600 mb-2">Preview</div>
          {error && <div className="text-red-600 mb-2">{error}</div>}
          <div className="border rounded max-h-44 overflow-auto">
            <table className="min-w-full">
              <thead className="bg-gray-50"><tr><th className="px-2 py-1 text-left">Type</th><th className="px-2 py-1 text-left">Model</th><th className="px-2 py-1 text-left">Serial</th><th className="px-2 py-1 text-left">Status</th><th className="px-2 py-1 text-left">Location</th></tr></thead>
              <tbody>
                {previewRows.map((r, i) => (<tr key={i}><td className="px-2 py-1 text-sm">{r.assetType}</td><td className="px-2 py-1 text-sm">{r.model}</td><td className="px-2 py-1 text-sm">{r.serialNumber}</td><td className="px-2 py-1 text-sm">{r.status}</td><td className="px-2 py-1 text-sm">{r.location}</td></tr>))}
                {previewRows.length === 0 && <tr><td className="p-4 text-sm text-gray-500" colSpan="5">No rows parsed yet</td></tr>}
              </tbody>
            </table>
          </div>
        </div>

        <div className="flex justify-end gap-2">
          <button className="px-3 py-1 bg-gray-200 rounded" onClick={onClose}>Cancel</button>
          <button className="px-3 py-1 bg-indigo-600 text-white rounded" onClick={handleUpload}>Upload {previewRows.length > 0 ? `(${previewRows.length})` : ""}</button>
        </div>
      </div>
    </div>
  );
}
