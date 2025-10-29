// src/controllers/InventoryController.jsx
import React, { useEffect, useMemo, useState } from "react";
import InventoryView from "../views/InventoryView";
import axios from "../services/axiosInstance";

/**
 * Inventory controller: fetches assets and exposes filter state to InventoryView
 */
export default function InventoryController() {
  const [assets, setAssets] = useState([]);
  const [loading, setLoading] = useState(false);
  const [filter, setFilter] = useState({ type: "", status: "", location: "", q: "" });
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAssets();
  }, []);

  async function fetchAssets() {
    setLoading(true);
    setError(null);
    try {
      const res = await axios.get(`/assets`);
      setAssets(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      setError(err?.response?.data || err.message || "Unknown error");
    } finally {
      setLoading(false);
    }
  }

  const filtered = useMemo(() => {
    const q = (filter.q || "").trim().toLowerCase();
    return assets
      .filter((a) => (filter.type ? (a.assetType || "").toString() === filter.type : true))
      .filter((a) => {
        if (!filter.status) return true;
        // compare case-insensitive, accept API enums or friendly labels
        const assetStatus = (a.status || "").toString().toLowerCase();
        return assetStatus === filter.status.toLowerCase();
      })
      .filter((a) => (filter.location ? (a.location || "").toLowerCase().includes(filter.location.toLowerCase()) : true))
      .filter((a) => {
        if (!q) return true;
        return ((a.serialNumber || "").toLowerCase().includes(q) || (a.model || "").toLowerCase().includes(q));
      });
  }, [assets, filter]);

  async function createAsset(newAsset) {
    try {
      const res = await axios.post(`/assets`, newAsset);
      fetchAssets(); // Refresh the list after creation
      return res.data;
    } catch (err) {
      setError(err?.response?.data || err.message || "Unknown error");
      throw err;
    }
  }

  async function updateAsset(id, updatedAsset) {
    try {
      const res = await axios.put(`/assets/${id}`, updatedAsset);
      fetchAssets(); // Refresh the list after update
      return res.data;
    } catch (err) {
      setError(err?.response?.data || err.message || "Unknown error");
      throw err;
    }
  }

  async function deleteAsset(id) {
    try {
      await axios.delete(`/assets/${id}`);
      fetchAssets(); // Refresh the list after deletion
    } catch (err) {
      setError(err?.response?.data || err.message || "Unknown error");
      throw err;
    }
  }

  return (
    <InventoryView
      assets={filtered}
      loading={loading}
      error={error}
      filter={filter}
      setFilter={setFilter}
      fetchAssets={fetchAssets}
      createAsset={createAsset}
      updateAsset={updateAsset}
      deleteAsset={deleteAsset}
    />
  );
}
