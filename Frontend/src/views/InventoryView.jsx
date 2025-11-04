// src/views/InventoryView.jsx
import React, { useState, useMemo, useRef, useCallback } from "react";
// import { AgGridReact } from 'ag-grid-react';
// import 'ag-grid-community/styles/ag-grid.css';
// import 'ag-grid-community/styles/ag-theme-alpine.css';
// import { ModuleRegistry, AllCommunityModules } from 'ag-grid-community';

// Register all community modules (v33 compatible)
// ModuleRegistry.registerModules(AllCommunityModules);
import DataTable from 'react-data-table-component';
import 'react-data-table-component-extensions/dist/index.css';
import './inventory-table.css'; // optional: you can create this for small styling tweaks
// Note: install dependency in Frontend: npm install react-data-table-component

import AssetStatusBadge from "../components/inventory/AssetStatusBadge";
import { Link } from "react-router-dom";

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

  const gridRef = useRef();

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

  // Columns for react-data-table-component
  const [globalFilter, setGlobalFilter] = useState('');

  const columns = useMemo(() => [
    { name: 'ID', selector: row => row.assetId, sortable: true, width: '110px' },
    { name: 'Serial', selector: row => row.serialNumber, sortable: true },
    { name: 'Type', selector: row => (row.assetType || '').toString().toUpperCase(), sortable: true },
    { name: 'Model', selector: row => row.model, sortable: true },
    { name: 'Status', selector: row => row.status, sortable: true, cell: row => <AssetStatusBadge status={row.status} /> },
    { name: 'Location', selector: row => row.location || '-', sortable: true },
    { name: 'Assigned To', selector: row => row.assignedToCustomer ? `${row.assignedToCustomer.name} (${row.assignedToCustomer.customerId ?? row.assignedToCustomer.id})` : '-', sortable: true },
    { name: 'Assigned Date', selector: row => row.assignedDate ? new Date(row.assignedDate).toLocaleString() : '-', sortable: true },
    { name: 'Actions', cell: row => (
        <div style={{display:'flex',gap:8}}>
          <button className="px-2 py-1 bg-blue-600 text-white rounded" onClick={() => handleEdit(row)}>Edit</button>
          <button className="px-2 py-1 bg-red-500 text-white rounded" onClick={() => { if(window.confirm('Delete asset?')) deleteAsset(row.assetId); }}>Delete</button>
        </div>
      ), ignoreRowClick: true }
  ], [deleteAsset]);

  const displayed = useMemo(() => {
    const q = (globalFilter || '').trim().toLowerCase();
    if (!q) return filtered;
    return (filtered || []).filter(r => (
      ((r.serialNumber||'') + ' ' + (r.model||'') + ' ' + (r.assetId||'')).toLowerCase().includes(q)
    ));
  }, [filtered, globalFilter]);

  const handleEdit = async (row) => {
    // Simple inline prompts for editing the most common fields
    try {
      const serial = window.prompt('Serial Number', row.serialNumber || '');
      if (serial === null) return;
      const model = window.prompt('Model', row.model || '');
      if (model === null) return;
      const status = window.prompt('Status (AVAILABLE,ASSIGNED,FAULTY,RETIRED)', row.status || 'AVAILABLE');
      if (status === null) return;
      const location = window.prompt('Location', row.location || '');
      if (location === null) return;
      const updated = { ...row, serialNumber: serial, model, status, location };
      await updateAsset(row.assetId, updated);
      fetchAssets?.();
    } catch (e) {
      console.error('Save failed', e);
      alert('Save failed: ' + (e?.message || 'unknown'));
    }
  };

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold">Inventory Manager</h1>
        <div>
          <Link to="/inventory/add" className="btn-primary">Add Infrastructure / Asset</Link>
        </div>
      </div>

      {error && <div className="mb-3 text-red-600">{String(error)}</div>}
      {loading && <div className="mb-3">Loading assets…</div>}

      <DataTable
        columns={columns}
        data={displayed}
        pagination
        paginationPerPage={25}
        // subHeader
        // subHeaderComponent={
        //   <div>
        //     <input
        //       type="text"
        //       placeholder="Search..."
        //       value={globalFilter}
        //       onChange={e => setGlobalFilter(e.target.value)}
        //       className="p-2 border rounded"
        //     />
        //   </div>
        // }
       />
    </div>
  );
}
