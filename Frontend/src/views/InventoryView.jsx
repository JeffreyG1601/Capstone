// src/views/InventoryView.jsx
import React, { useState, useMemo } from "react";
import AddAssetModal from "../components/inventory/AddAssetModal";
import BulkUploadModal from "../components/inventory/BulkUploadModal";
import AssetStatusBadge from "../components/inventory/AssetStatusBadge";
import SplitterForm from "../components/inventory/SplitterForm";
import FDHForm from "../components/inventory/FDHForm";
import HeadendForm from "../components/inventory/HeadendForm";

/**
 * Props expected from InventoryController:
 *  - assets, loading, error, filter, setFilter, fetchAssets, createAsset, updateAsset, deleteAsset
 */
export default function InventoryView({
  assets,
  loading,
  error,
  filter,
  setFilter,
  fetchAssets,
  createAsset,
  updateAsset,
  deleteAsset,
}) {
  const [editingAsset, setEditingAsset] = useState(null);
  const [editForm, setEditForm] = useState({});
  const [showAddModal, setShowAddModal] = useState(false);
  const [showBulkModal, setShowBulkModal] = useState(false);
  const [showSplitterForm, setShowSplitterForm] = useState(false);
  const [showFDHForm, setShowFDHForm] = useState(false);
  const [showHeadendForm, setShowHeadendForm] = useState(false);
  const [selectedType, setSelectedType] = useState("");

  // derived filtered assets (small client-side filters for demo)
  const filtered = useMemo(() => {
    const q = (filter.q || "").trim().toLowerCase();
    return (assets || [])
      .filter((a) => (filter.type ? (a.assetType || "").toString() === filter.type : true))
      .filter((a) => {
        if (!filter.status) return true;
        const assetStatus = (a.status || "").toString().toUpperCase();
        return assetStatus === filter.status.toUpperCase();
      })
      .filter((a) => (filter.location ? (a.location || "").toLowerCase().includes(filter.location.toLowerCase()) : true))
      .filter((a) => {
        if (!q) return true;
        return (
          ((a.serialNumber || "") + " " + (a.model || "") + " " + (a.assetId || ""))
            .toLowerCase()
            .includes(q)
        );
      });
  }, [assets, filter]);

  const startEdit = (asset) => {
    setEditingAsset(asset.assetId);
    setEditForm({ ...asset });
  };

  const cancelEdit = () => {
    setEditingAsset(null);
    setEditForm({});
  };

  const saveEdit = async () => {
    try {
      await updateAsset(editingAsset, editForm);
      cancelEdit();
    } catch (e) {
      // InventoryController already sets error; we keep UI simple
      console.error(e);
      alert("Save failed: " + (e?.message || "unknown"));
    }
  };

  const bulkChangeStatus = async (newStatus) => {
    const checked = Array.from(document.querySelectorAll("input[name=bulkSelect]:checked")).map(
      (i) => i.value
    );
    if (checked.length === 0) return alert("Select at least one asset.");
    if (!window.confirm(`Change status of ${checked.length} assets to ${newStatus}?`)) return;
    try {
      // naive approach: call updateAsset for each
      for (const id of checked) {
        const asset = assets.find((a) => String(a.assetId) === String(id));
        if (!asset) continue;
        const payload = { ...asset, status: newStatus };
        // use updateAsset prop (which calls backend)
        // eslint-disable-next-line no-await-in-loop
        await updateAsset(id, payload);
      }
      fetchAssets();
      alert(`Updated ${checked.length} asset(s).`);
    } catch (e) {
      console.error(e);
      alert("Bulk update failed: " + (e?.message || e));
    }
  };

  const toggleSelectAll = (ev) => {
    const all = document.querySelectorAll("input[name=bulkSelect]");
    all.forEach((c) => (c.checked = ev.target.checked));
  };

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold">Inventory Manager</h1>
        <div className="flex gap-2">
          <button className="px-3 py-1 bg-indigo-600 text-white rounded" onClick={() => setShowAddModal(true)}>Add Asset</button>
          <button className="px-3 py-1 bg-gray-100 rounded" onClick={() => setShowBulkModal(true)}>Bulk Upload (CSV)</button>
          <div className="dropdown inline-block relative">
            <button className="px-3 py-1 bg-gray-50 rounded border">Add Infrastructure</button>
            <div className="absolute right-0 mt-2 w-48 bg-white border rounded shadow p-2">
              <button className="block w-full text-left px-2 py-1 hover:bg-gray-50" onClick={() => setShowSplitterForm(true)}>Add Splitter</button>
              <button className="block w-full text-left px-2 py-1 hover:bg-gray-50" onClick={() => setShowFDHForm(true)}>Add FDH</button>
              <button className="block w-full text-left px-2 py-1 hover:bg-gray-50" onClick={() => setShowHeadendForm(true)}>Add Headend</button>
            </div>
          </div>
        </div>
      </div>

      <div className="mb-4">
        <form className="flex gap-2 items-center" onSubmit={(e) => { e.preventDefault(); fetchAssets(); }}>
          <input placeholder="Search serial / model / id" value={filter.q} onChange={(e)=> setFilter(f=>({...f, q: e.target.value}))} className="border px-2 py-1 rounded w-64" />
          <select value={filter.type} onChange={(e)=> { setFilter(f=>({...f, type: e.target.value})); setSelectedType(e.target.value); }} className="border px-2 py-1 rounded">
            <option value="">All types</option>
            <option value="ONT">ONT</option>
            <option value="ROUTER">Router</option>
            <option value="ETHERNET_SWITCH">Ethernet Switch</option>
            <option value="SPLITTER">Splitter</option>
            <option value="FDH">FDH</option>
            <option value="HEADEND">Headend</option>
          </select>
          <select value={filter.status} onChange={(e)=> setFilter(f=>({...f, status: e.target.value}))} className="border px-2 py-1 rounded">
            <option value="">All statuses</option>
            <option value="AVAILABLE">Available</option>
            <option value="ASSIGNED">Assigned</option>
            <option value="FAULTY">Faulty</option>
            <option value="RETIRED">Retired</option>
          </select>
          <input placeholder="Location" value={filter.location} onChange={(e)=> setFilter(f=>({...f, location: e.target.value}))} className="border px-2 py-1 rounded" />
          <button className="px-3 py-1 bg-gray-200 rounded">Filter</button>
        </form>
      </div>

      {error && <div className="mb-3 text-red-600">{String(error)}</div>}
      {loading && <div className="mb-3">Loading assets…</div>}

      <div className="overflow-x-auto border rounded">
        <table className="min-w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-3 py-2 text-left text-sm"><input type="checkbox" onChange={toggleSelectAll} /></th>
              <th className="px-3 py-2 text-left text-sm">ID</th>
              <th className="px-3 py-2 text-left text-sm">Serial</th>
              <th className="px-3 py-2 text-left text-sm">Type</th>
              <th className="px-3 py-2 text-left text-sm">Model</th>
              <th className="px-3 py-2 text-left text-sm">Status</th>
              <th className="px-3 py-2 text-left text-sm">Location</th>
              <th className="px-3 py-2 text-left text-sm">Assigned To</th>
              <th className="px-3 py-2 text-left text-sm">Assigned Date</th>
              <th className="px-3 py-2 text-left text-sm">Actions</th>
            </tr>
          </thead>

          <tbody className="bg-white">
            {filtered.map((a) => (
              <tr key={a.assetId} className="hover:bg-gray-50">
                <td className="px-3 py-2 text-sm">
                  <input name="bulkSelect" type="checkbox" value={a.assetId} />
                </td>

                <td className="px-3 py-2 text-sm">{a.assetId}</td>

                <td className="px-3 py-2 text-sm">
                  {editingAsset === a.assetId ? (
                    <input className="border px-2 py-1 rounded" value={editForm.serialNumber || ""} onChange={(e)=> setEditForm(prev=> ({...prev, serialNumber: e.target.value}))} />
                  ) : (
                    a.serialNumber
                  )}
                </td>

                <td className="px-3 py-2 text-sm uppercase">{a.assetType}</td>

                <td className="px-3 py-2 text-sm">
                  {editingAsset === a.assetId ? (
                    <input className="border px-2 py-1 rounded" value={editForm.model || ""} onChange={(e)=> setEditForm(prev=> ({...prev, model: e.target.value}))} />
                  ) : (
                    a.model
                  )}
                </td>

                <td className="px-3 py-2 text-sm">
                  {editingAsset === a.assetId ? (
                    <select className="border px-2 py-1 rounded" value={editForm.status || ""} onChange={(e)=> setEditForm(prev=> ({...prev, status: e.target.value}))}>
                      <option value="AVAILABLE">Available</option>
                      <option value="ASSIGNED">Assigned</option>
                      <option value="FAULTY">Faulty</option>
                      <option value="RETIRED">Retired</option>
                    </select>
                  ) : (
                    <AssetStatusBadge status={a.status} />
                  )}
                </td>

                <td className="px-3 py-2 text-sm">
                  {editingAsset === a.assetId ? (
                    <input className="border px-2 py-1 rounded" value={editForm.location || ""} onChange={(e)=> setEditForm(prev=> ({...prev, location: e.target.value}))} />
                  ) : (
                    a.location || "-"
                  )}
                </td>

                <td className="px-3 py-2 text-sm">
                  {a.assignedToCustomer ? `${a.assignedToCustomer.name} (${a.assignedToCustomer.customerId ?? a.assignedToCustomer.id})` : "-"}
                </td>

                <td className="px-3 py-2 text-sm">{a.assignedDate ? new Date(a.assignedDate).toLocaleString() : "-"}</td>

                <td className="px-3 py-2 text-sm">
                  {editingAsset === a.assetId ? (
                    <>
                      <button className="px-2 py-1 bg-green-600 text-white rounded mr-2" onClick={saveEdit}>Save</button>
                      <button className="px-2 py-1 bg-gray-400 text-white rounded" onClick={cancelEdit}>Cancel</button>
                    </>
                  ) : (
                    <>
                      <button className="px-2 py-1 bg-blue-600 text-white rounded mr-2" onClick={() => startEdit(a)}>Edit</button>
                      <button className="px-2 py-1 bg-red-500 text-white rounded" onClick={()=> { if(window.confirm("Delete asset?")) deleteAsset(a.assetId); }}>Delete</button>
                    </>
                  )}
                </td>
              </tr>
            ))}

            {filtered.length === 0 && !loading && (
              <tr>
                <td className="p-4 text-sm text-gray-500" colSpan="10">No assets found.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <div className="mt-4 flex items-center gap-2">
        <label className="text-sm mr-2">Bulk actions:</label>
        <select onChange={(e)=> bulkChangeStatus(e.target.value)} defaultValue="">
          <option value="">Set status to…</option>
          <option value="AVAILABLE">Available</option>
          <option value="ASSIGNED">Assigned</option>
          <option value="FAULTY">Faulty</option>
          <option value="RETIRED">Retired</option>
        </select>
      </div>

      {/* Modals & forms */}
      <AddAssetModal isOpen={showAddModal} onClose={() => setShowAddModal(false)} onCreate={async (payload) => { await createAsset(payload); setShowAddModal(false); fetchAssets(); }} />
      <BulkUploadModal isOpen={showBulkModal} onClose={() => setShowBulkModal(false)} onUpload={async (rows) => { // rows = [{assetType,model,serialNumber,status,location}]
        // create each row
        for (const r of rows) {
          try {
            // createAsset exists on controller (calls axios)
            // eslint-disable-next-line no-await-in-loop
            await createAsset(r);
          } catch (e) {
            console.error("bulk row failed", e, r);
          }
        }
        setShowBulkModal(false); fetchAssets();
      }} />
      <SplitterForm isOpen={showSplitterForm} onClose={() => setShowSplitterForm(false)} onCreate={async (payload) => { try { await fetch("/api/splitters", { method: "POST", headers: { 'Content-Type':'application/json' }, body: JSON.stringify(payload) }); alert("Splitter (frontend) created (API call attempted)."); } catch(e) { alert("Splitter saved locally (no API)"); } setShowSplitterForm(false); fetchAssets(); }} />
      <FDHForm isOpen={showFDHForm} onClose={() => setShowFDHForm(false)} onCreate={async (payload) => { try { await fetch("/api/fdhs", { method: "POST", headers: { 'Content-Type':'application/json' }, body: JSON.stringify(payload) }); alert("FDH (frontend) created (API call attempted)."); } catch(e) { alert("FDH saved locally (no API)"); } setShowFDHForm(false); fetchAssets(); }} />
      <HeadendForm isOpen={showHeadendForm} onClose={() => setShowHeadendForm(false)} onCreate={async (payload) => { try { await fetch("/api/headends", { method: "POST", headers: { 'Content-Type':'application/json' }, body: JSON.stringify(payload) }); alert("Headend (frontend) created (API call attempted)."); } catch(e) { alert("Headend saved locally (no API)"); } setShowHeadendForm(false); fetchAssets(); }} />
    </div>
  );
}
